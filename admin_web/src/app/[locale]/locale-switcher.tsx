"use client";

import { usePathname, useRouter } from "next/navigation";
import { useLocale } from "next-intl";

export default function LocaleSwitcher() {
  const locale = useLocale();
  const router = useRouter();
  const pathname = usePathname();

  function switchTo(next: string) {
    const segments = pathname.split("/");
    segments[1] = next;
    router.push(segments.join("/"));
  }

  return (
    <div>
      <button
        onClick={() => switchTo("en")}
        disabled={locale === "en"}
        type="button"
      >
        EN
      </button>
      <button
        onClick={() => switchTo("ar")}
        disabled={locale === "ar"}
        type="button"
      >
        AR
      </button>
    </div>
  );
}
