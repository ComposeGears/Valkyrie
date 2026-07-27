export type ConvertResult = {
  success: boolean;
  iconName: string;
  fileName: string;
  code: string;
  error?: string;
};

export type ConvertResultWithSvg = ConvertResult & {
  svg?: string;
  sourceId?: string;
};

export type StatusType = "ready" | "working" | "warning" | "error";
