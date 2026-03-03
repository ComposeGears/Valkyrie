import type { ConvertResult } from "../features/converterAdapter";

export type ConvertResultWithSvg = ConvertResult & { svg?: string };

export type StatusType = "ready" | "working" | "warning" | "error";
