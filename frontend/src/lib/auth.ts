import Cookies from "js-cookie";

const TOKEN_KEY = "cc_token";
const ROLE_KEY  = "cc_role";
const USER_KEY  = "cc_user";

// secure:true silently drops cookies on http://localhost — only use in prod
const IS_PROD = process.env.NEXT_PUBLIC_PRODUCTION_MODE === "true";

export const setToken   = (token: string) =>
  Cookies.set(TOKEN_KEY, token, { expires: 1, secure: IS_PROD, sameSite: "lax" });
export const getToken   = () => Cookies.get(TOKEN_KEY);
export const removeToken = () => Cookies.remove(TOKEN_KEY);

export const setRole   = (role: string) =>
  Cookies.set(ROLE_KEY, role, { expires: 1, secure: IS_PROD, sameSite: "lax" });
export const getRole   = () => Cookies.get(ROLE_KEY);
export const removeRole = () => Cookies.remove(ROLE_KEY);

export const setUser   = (user: object) =>
  localStorage.setItem(USER_KEY, JSON.stringify(user));
export const getUser   = () => {
  try { return JSON.parse(localStorage.getItem(USER_KEY) || "null"); }
  catch { return null; }
};
export const removeUser = () => localStorage.removeItem(USER_KEY);

export const logout = () => {
  removeToken();
  removeRole();
  removeUser();
  window.location.href = "/login";
};

export const isAuthenticated = () => !!getToken();
