import type { ConvertResult } from "./converterAdapter";

export type ConvertResultWithSvg = ConvertResult & { svg?: string };

export type StatusType = "ready" | "working" | "warning" | "error";
