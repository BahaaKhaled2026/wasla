import { getTranslations } from "next-intl/server";
import styles from "./page.module.css";

interface StatusResponse {
  requestId: string;
  buildRevision: string;
  serverTime: string;
  health: {
    status: string;
    components: Record<string, string>;
  };
}

async function fetchStatus(): Promise<StatusResponse | null> {
  try {
    const res = await fetch("http://localhost:8080/api/v1/status", {
      cache: "no-store",
    });
    if (!res.ok) return null;
    return (await res.json()) as StatusResponse;
  } catch {
    return null;
  }
}

export default async function StatusPage() {
  const t = await getTranslations("status");
  const status = await fetchStatus();
  const healthy = status?.health.status === "UP";

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>{t("title")}</h1>
      <div className={healthy ? styles.cardHealthy : styles.cardUnhealthy}>
        <p className={styles.statusLine}>
          {healthy ? t("healthy") : t("unhealthy")}
        </p>
        {status && (
          <>
            <dl className={styles.details}>
              <dt>{t("requestId")}</dt>
              <dd>{status.requestId}</dd>
              <dt>{t("checkedAt")}</dt>
              <dd>{status.serverTime}</dd>
              <dt>Build</dt>
              <dd>{status.buildRevision}</dd>
            </dl>
            <ul className={styles.componentList}>
              {Object.entries(status.health.components).map(([name, value]) => (
                <li key={name}>
                  {name}: {value}
                </li>
              ))}
            </ul>
          </>
        )}
      </div>
    </div>
  );
}
