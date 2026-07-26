function toDartIdentifier(path) {
  const [first, ...rest] = path;
  const camelRest = rest
    .map((segment) =>
      segment
        .split("-")
        .map((part, i) =>
          i === 0 ? part : part.charAt(0).toUpperCase() + part.slice(1),
        )
        .join(""),
    )
    .map((s) => s.charAt(0).toUpperCase() + s.slice(1));
  return first + camelRest.join("");
}

function dartValue(token) {
  if (token.type === "color") {
    const hex = token.value.replace("#", "").toUpperCase();
    const argb =
      hex.length === 6
        ? "FF" + hex
        : hex.length === 8
          ? hex.slice(6) + hex.slice(0, 6)
          : hex;
    return `Color(0x${argb})`;
  }
  if (token.type === "dimension") {
    const num = parseFloat(token.value);
    return `${num}`;
  }
  if (token.type === "number" || token.type === "fontWeight") {
    return `${token.value}`;
  }
  if (token.type === "duration") {
    const ms = parseInt(token.value, 10);
    return `Duration(milliseconds: ${ms})`;
  }
  return `'${token.value}'`;
}

function dartType(token) {
  if (token.type === "color") return "Color";
  if (token.type === "dimension") return "double";
  if (token.type === "number") return "double";
  if (token.type === "fontWeight") return "int";
  if (token.type === "duration") return "Duration";
  return "String";
}

export const dartThemeFormat = {
  name: "flutter/class.dart",
  format: ({ dictionary }) => {
    const header = `// GENERATED FILE - DO NOT EDIT BY HAND.
// Source: packages/design-tokens/tokens/*.json
// Regenerate with: npm run build (inside packages/design-tokens)
import 'package:flutter/widgets.dart';

class WaslaTokens {
  WaslaTokens._();

`;
    const body = dictionary.allTokens
      .map((token) => {
        const name = toDartIdentifier(token.path);
        return `  static const ${dartType(token)} ${name} = ${dartValue(token)};`;
      })
      .join("\n");

    return `${header}${body}\n}\n`;
  },
};
