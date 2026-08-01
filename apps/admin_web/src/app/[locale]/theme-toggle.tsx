"use client";

import { useEffect, useState } from "react";
import { useTranslations } from "next-intl";

type Theme = "light" | "dark";

export default function ThemeToggle() {
  const t = useTranslations("theme");
  const [theme, setTheme] = useState<Theme>("light");

  useEffect(() => {
    const stored = window.localStorage.getItem("wasla-theme") as Theme | null;
    const initial = stored ?? "light";
    setTheme(initial);
    document.documentElement.setAttribute("data-theme", initial);
  }, []);

  function toggle() {
    const next: Theme = theme === "light" ? "dark" : "light";
    setTheme(next);
    document.documentElement.setAttribute("data-theme", next);
    window.localStorage.setItem("wasla-theme", next);
  }

  return (
    <button onClick={toggle} type="button">
      {theme === "light" ? t("dark") : t("light")}
    </button>
  );
}
