import axios from "axios";
import { getToken, logout } from "./auth";

const IS_PROD = process.env.NEXT_PUBLIC_PRODUCTION_MODE === "true";

const api = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
  timeout: 15000,
});

// ── Request interceptor: attach JWT + debug log
api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;

  if (!IS_PROD) {
    console.groupCollapsed(
      `%c⬆ ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`,
      "color:#6366f1;font-weight:bold"
    );
    console.log("URL    :", `${config.baseURL}${config.url}`);
    if (config.params)  console.log("Params :", config.params);
    if (config.data)    console.log("Body   :", typeof config.data === "string" ? JSON.parse(config.data) : config.data);
    console.log("Headers:", config.headers);
    console.groupEnd();
  }

  return config;
});

// ── Response interceptor: debug log + 401 auto-logout
api.interceptors.response.use(
  (res) => {
    if (!IS_PROD) {
      console.groupCollapsed(
        `%c⬇ ${res.status} ${res.config.method?.toUpperCase()} ${res.config.baseURL}${res.config.url}`,
        "color:#22c55e;font-weight:bold"
      );
      console.log("Status  :", res.status, res.statusText);
      console.log("Response:", res.data);
      console.groupEnd();
    }
    return res;
  },
  (err) => {
    if (!IS_PROD) {
      console.groupCollapsed(
        `%c✖ ${err?.response?.status ?? "ERR"} ${err?.config?.method?.toUpperCase()} ${err?.config?.baseURL}${err?.config?.url}`,
        "color:#ef4444;font-weight:bold"
      );
      console.log("Status  :", err?.response?.status, err?.response?.statusText);
      console.log("Response:", err?.response?.data);
      console.log("Message :", err?.message);
      console.groupEnd();
    }
    if (err.response?.status === 401) logout();
    return Promise.reject(err);
  }
);

export default api;
