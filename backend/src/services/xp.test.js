import {
  applyXp,
  workoutXp,
  foodXp,
  xpToNextLevel,
  avatarStageForLevel,
  streakBonusXp,
} from './xp.js';

describe('workoutXp', () => {
  test('zero volume awards exactly 20 base XP', () => {
    expect(workoutXp(0)).toBe(20);
  });

  test('5000 kg volume awards 25 XP', () => {
    expect(workoutXp(5000)).toBe(25);
  });

  test('999 kg volume still awards only base 20 XP', () => {
    expect(workoutXp(999)).toBe(20);
  });

  test('handles non-finite input safely', () => {
    expect(workoutXp(NaN)).toBe(20);
    expect(workoutXp(-100)).toBe(20);
  });
});

describe('foodXp', () => {
  test('always 5', () => {
    expect(foodXp()).toBe(5);
  });
});

describe('streakBonusXp', () => {
  test('awards streak * 2', () => {
    expect(streakBonusXp(7)).toBe(14);
  });
  test('zero or negative is zero', () => {
    expect(streakBonusXp(0)).toBe(0);
    expect(streakBonusXp(-3)).toBe(0);
  });
});

describe('xpToNextLevel', () => {
  test('level 1 -> 100 XP needed', () => {
    expect(xpToNextLevel(1)).toBe(100);
  });
  test('grows roughly with level^1.5', () => {
    expect(xpToNextLevel(4)).toBe(800);
  });
});

describe('avatarStageForLevel', () => {
  test.each([
    [1, 0], [4, 0],
    [5, 1], [14, 1],
    [15, 2], [29, 2],
    [30, 3], [99, 3],
  ])('level %i -> stage %i', (lvl, stage) => {
    expect(avatarStageForLevel(lvl)).toBe(stage);
  });
});

describe('applyXp leveling', () => {
  test('single level-up with exact XP', () => {
    const res = applyXp({ level: 1, xp: 0, xpToNext: 100 }, 100);
    expect(res.level).toBe(2);
    expect(res.xp).toBe(0);
    expect(res.leveledUp).toBe(true);
    expect(res.levelsGained).toBe(1);
  });

  test('partial XP does not level up', () => {
    const res = applyXp({ level: 1, xp: 0, xpToNext: 100 }, 99);
    expect(res.level).toBe(1);
    expect(res.xp).toBe(99);
    expect(res.leveledUp).toBe(false);
  });

  test('multi-level chain (1 -> 2 -> 3)', () => {
    // level 1 needs 100, level 2 needs round(100 * 2^1.5) = 283
    const res = applyXp({ level: 1, xp: 0, xpToNext: 100 }, 400);
    expect(res.level).toBe(3);
    expect(res.levelsGained).toBe(2);
    expect(res.leveledUp).toBe(true);
  });

  test('reports new avatar stage on threshold cross', () => {
    // sum of level-up XP from 1->5
    const totalToFive = [1, 2, 3, 4].reduce((s, l) => s + xpToNextLevel(l), 0);
    const res = applyXp({ level: 1, xp: 0, xpToNext: 100 }, totalToFive);
    expect(res.level).toBe(5);
    expect(res.avatarStage).toBe(1); // Fighter
  });
});
