import { classifyStreakTransition, nextStreakDays } from './streaks.js';

const day = (iso) => new Date(`${iso}T12:00:00Z`);

describe('classifyStreakTransition', () => {
  test('first activity ever', () => {
    expect(classifyStreakTransition(null, day('2026-05-14'))).toBe('first');
  });

  test('yesterday increments', () => {
    expect(
      classifyStreakTransition(day('2026-05-13'), day('2026-05-14')),
    ).toBe('increment');
  });

  test('same day is no change', () => {
    expect(
      classifyStreakTransition(day('2026-05-14'), day('2026-05-14')),
    ).toBe('same');
  });

  test('two days ago resets', () => {
    expect(
      classifyStreakTransition(day('2026-05-12'), day('2026-05-14')),
    ).toBe('reset');
  });

  test('a week ago resets', () => {
    expect(
      classifyStreakTransition(day('2026-05-07'), day('2026-05-14')),
    ).toBe('reset');
  });
});

describe('nextStreakDays', () => {
  test('first sets streak to 1', () => {
    expect(nextStreakDays(0, 'first')).toBe(1);
  });

  test('increment grows streak', () => {
    expect(nextStreakDays(6, 'increment')).toBe(7);
  });

  test('same keeps streak', () => {
    expect(nextStreakDays(10, 'same')).toBe(10);
  });

  test('reset returns to 1', () => {
    expect(nextStreakDays(99, 'reset')).toBe(1);
  });
});
