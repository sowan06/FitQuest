import request from 'supertest';

const hasTestDb = !!process.env.TEST_DATABASE_URL;
const describeOrSkip = hasTestDb ? describe : describe.skip;

if (hasTestDb && !process.env.DATABASE_URL) {
  process.env.DATABASE_URL = process.env.TEST_DATABASE_URL;
}
if (hasTestDb && !process.env.JWT_SECRET) {
  process.env.JWT_SECRET = 'test_secret_min_32_characters_long_xxx';
}

describeOrSkip('workout flow', () => {
  let app;
  let resetDatabase;
  let closeDatabase;
  let token;

  beforeAll(async () => {
    ({ app } = await import('../src/app.js'));
    ({ resetDatabase, closeDatabase } = await import('./helpers.js'));
    await resetDatabase();
  });

  beforeEach(async () => {
    await resetDatabase();
    const reg = await request(app)
      .post('/auth/register')
      .send({ username: 'lifter', email: 'lifter@test.com', password: 'StrongPass123!' });
    token = reg.body.token;
  });

  afterAll(async () => {
    await closeDatabase();
  });

  test('logging a workout awards XP and updates character', async () => {
    const res = await request(app)
      .post('/workouts')
      .set('Authorization', `Bearer ${token}`)
      .send({
        notes: 'Chest day',
        sets: [
          { exercise_name: 'Bench Press', reps: 8, weight_kg: 80 },
          { exercise_name: 'Bench Press', reps: 8, weight_kg: 80 },
        ],
      });
    expect(res.status).toBe(201);
    expect(res.body.xp_earned).toBeGreaterThan(0);
    expect(res.body.total_volume).toBe(8 * 80 * 2);

    const ch = await request(app)
      .get('/character')
      .set('Authorization', `Bearer ${token}`);
    expect(ch.body.xp).toBeGreaterThan(0);
    expect(ch.body.total_workouts).toBe(1);
  });

  test('rejects empty session', async () => {
    const res = await request(app)
      .post('/workouts')
      .set('Authorization', `Bearer ${token}`)
      .send({ sets: [] });
    expect(res.status).toBe(400);
  });

  test('daily workout quest progresses on log', async () => {
    await request(app)
      .post('/workouts')
      .set('Authorization', `Bearer ${token}`)
      .send({ sets: [{ exercise_name: 'Squat', reps: 5, weight_kg: 100 }] });

    const quests = await request(app)
      .get('/quests')
      .set('Authorization', `Bearer ${token}`);
    const daily = quests.body.find((q) => q.quest_id === 'daily_log_workout');
    expect(daily).toBeTruthy();
    expect(daily.status).toBe('completed');
    expect(daily.progress).toBe(1);
  });
});
