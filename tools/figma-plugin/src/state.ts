import type { ConvertResultWithSvg } from "./types";

const conversionResults: ConvertResultWithSvg[] = [];

export function getConversionResults(): ReadonlyArray<ConvertResultWithSvg> {
  return conversionResults;
}

export function getConversionResultsCount(): number {
  return conversionResults.length;
}

export function clearConversionResults(): void {
  conversionResults.length = 0;
}

export function replaceConversionResults(results: ConvertResultWithSvg[]): void {
  conversionResults.length = 0;
  conversionResults.push(...results);
}

export function hasSuccessfulConversionResults(): boolean {
  return conversionResults.some((item) => item.ok);
}

export function getSuccessfulConversionResults(): ConvertResultWithSvg[] {
  return conversionResults.filter((item) => item.ok);
}
