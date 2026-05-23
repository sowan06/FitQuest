import {
  computeStr,
  computeEnd,
  computeVit,
  computeWis,
  computeCon,
} from './stats.js';

describe('stat thresholds', () => {
  test('STR baseline = 1', () => {
    expect(computeStr(0)).toBe(1);
  });

  test('STR +1 every 10,000 kg', () => {
    expect(computeStr(9_999)).toBe(1);
    expect(computeStr(10_000)).toBe(2);
    expect(computeStr(25_000)).toBe(3);
  });

  test('END +1 every 10 workouts', () => {
    expect(computeEnd(9)).toBe(1);
    expect(computeEnd(10)).toBe(2);
    expect(computeEnd(50)).toBe(6);
  });

  test('VIT +1 every 7-day streak', () => {
    expect(computeVit(6)).toBe(1);
    expect(computeVit(7)).toBe(2);
    expect(computeVit(28)).toBe(5);
  });

  test('WIS +1 every 14 protein days', () => {
    expect(computeWis(13)).toBe(1);
    expect(computeWis(14)).toBe(2);
  });

  test('CON +1 every 14 calorie days', () => {
    expect(computeCon(13)).toBe(1);
    expect(computeCon(14)).toBe(2);
  });
});
