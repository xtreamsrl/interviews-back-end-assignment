export function calculateTotalDiscount(userPoints: number): number {
  const totalDiscount = Math.floor(userPoints / 25);
  return totalDiscount;
}
