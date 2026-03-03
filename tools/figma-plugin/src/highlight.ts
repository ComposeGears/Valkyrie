import { escapeHtml } from "./utils";

const KEYWORDS = new Set([
  "package",
  "import",
  "class",
  "object",
  "interface",
  "fun",
  "val",
  "var",
  "return",
  "if",
  "else",
  "when",
  "for",
  "while",
  "do",
  "try",
  "catch",
  "finally",
  "throw",
  "in",
  "is",
  "as",
  "this",
  "super",
  "true",
  "false",
  "null",
  "private",
  "public",
  "internal",
  "protected",
  "override",
  "abstract",
  "open",
  "sealed",
  "data",
  "enum",
  "inline",
  "crossinline",
  "noinline",
  "suspend",
  "operator",
  "infix",
  "const",
  "lateinit",
  "tailrec",
  "reified",
  "by",
  "where",
  "typealias",
]);

function wrap(type: string, value: string): string {
  return `<span class="tok-${type}">${escapeHtml(value)}</span>`;
}

export function highlightKotlin(source: string): string {
  let i = 0;
  const out: string[] = [];

  while (i < source.length) {
    const rest = source.slice(i);

    const ws = rest.match(/^\s+/);
    if (ws) {
      out.push(escapeHtml(ws[0]));
      i += ws[0].length;
      continue;
    }

    const lineComment = rest.match(/^\/\/[^\n]*/);
    if (lineComment) {
      out.push(wrap("comment", lineComment[0]));
      i += lineComment[0].length;
      continue;
    }

    const blockComment = rest.match(/^\/\*[\s\S]*?\*\//);
    if (blockComment) {
      out.push(wrap("comment", blockComment[0]));
      i += blockComment[0].length;
      continue;
    }

    const tripleQuoted = rest.match(/^"""[\s\S]*?"""/);
    if (tripleQuoted) {
      out.push(wrap("string", tripleQuoted[0]));
      i += tripleQuoted[0].length;
      continue;
    }

    const quoted = rest.match(/^"(?:\\.|[^"\\])*"/);
    if (quoted) {
      out.push(wrap("string", quoted[0]));
      i += quoted[0].length;
      continue;
    }

    const charLiteral = rest.match(/^'(?:\\.|[^'\\])'/);
    if (charLiteral) {
      out.push(wrap("string", charLiteral[0]));
      i += charLiteral[0].length;
      continue;
    }

    const annotation = rest.match(/^@[A-Za-z_][A-Za-z0-9_.]*/);
    if (annotation) {
      out.push(wrap("annotation", annotation[0]));
      i += annotation[0].length;
      continue;
    }

    const number = rest.match(/^\b(?:0[xX][0-9a-fA-F]+|\d+(?:\.\d+)?(?:[eE][+-]?\d+)?)\b/);
    if (number) {
      out.push(wrap("number", number[0]));
      i += number[0].length;
      continue;
    }

    const identifier = rest.match(/^[A-Za-z_][A-Za-z0-9_]*/);
    if (identifier) {
      const token = identifier[0];
      if (KEYWORDS.has(token)) {
        out.push(wrap("keyword", token));
      } else if (/^[A-Z]/.test(token)) {
        out.push(wrap("type", token));
      } else {
        out.push(escapeHtml(token));
      }
      i += token.length;
      continue;
    }

    out.push(escapeHtml(source[i]));
    i += 1;
  }

  return out.join("");
}
