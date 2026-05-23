/**
 * Auth integration tests.
 * Skipped automatically if TEST_DATABASE_URL is not set.
 */
import request from 'supertest';

const hasTestDb = !!process.env.TEST_DATABASE_URL;
const describeOrSkip = hasTestDb ? describe : describe.skip;

if (hasTestDb && !process.env.DATABASE_URL) {
  process.env.DATABASE_URL = process.env.TEST_DATABASE_URL;
}
if (hasTestDb && !process.env.JWT_SECRET) {
  process.env.JWT_SECRET = 'test_secret_min_32_characters_long_xxx';
}

describeOrSkip('auth flow', () => {
  let app;
  let resetDatabase;
  let closeDatabase;

  beforeAll(async () => {
    ({ app } = await import('../src/app.js'));
    ({ resetDatabase, closeDatabase } = await import('./helpers.js'));
    await resetDatabase();
  });

  afterEach(async () => {
    await resetDatabase();
  });

  afterAll(async () => {
    await closeDatabase();
  });

  test('register returns a token and creates a character', async () => {
    const res = await request(app)
      .post('/auth/register')
      .send({ username: 'hero1', email: 'hero1@test.com', password: 'StrongPass123!' });
    expect(res.status).toBe(201);
    expect(res.body.token).toMatch(/^ey/);

    const me = await request(app)
      .get('/auth/me')
      .set('Authorization', `Bearer ${res.body.token}`);
    expect(me.status).toBe(200);
    expect(me.body.username).toBe('hero1');
    expect(me.body.password_hash).toBeUndefined();

    const ch = await request(app)
      .get('/character')
      .set('Authorization', `Bearer ${res.body.token}`);
    expect(ch.status).toBe(200);
    expect(ch.body.level).toBe(1);
    expect(ch.body.avatar_label).toBe('Rookie');
  });

  test('login with wrong password returns 401', async () => {
    await request(app)
      .post('/auth/register')
      .send({ username: 'hero2', email: 'hero2@test.com', password: 'StrongPass123!' });

    const bad = await request(app)
      .post('/auth/login')
      .send({ email: 'hero2@test.com', password: 'wrong' });
    expect(bad.status).toBe(401);
  });

  test('protected route rejects missing token', async () => {
    const res = await request(app).get('/character');
    expect(res.status).toBe(401);
  });
});
