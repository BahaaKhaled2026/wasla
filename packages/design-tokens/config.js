import StyleDictionary from "style-dictionary";
import { dartThemeFormat } from "./formats/dart-theme.js";

StyleDictionary.registerFormat(dartThemeFormat);

export default {
  source: ["tokens/**/*.json"],
  platforms: {
    css: {
      transformGroup: "css",
      buildPath: "build/css/",
      files: [
        {
          destination: "tokens.css",
          format: "css/variables",
          options: { selector: ":root" },
        },
      ],
    },
    ts: {
      transformGroup: "js",
      buildPath: "build/ts/",
      files: [
        {
          destination: "tokens.ts",
          format: "javascript/es6",
        },
      ],
    },
    dart: {
      transformGroup: "js",
      buildPath: "../wasla_design_system/lib/src/generated/",
      files: [
        {
          destination: "wasla_tokens.dart",
          format: "flutter/class.dart",
        },
      ],
    },
  },
};
