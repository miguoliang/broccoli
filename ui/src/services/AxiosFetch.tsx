import axios, { AxiosError } from "axios";
import { appConfig } from "configs";
import { toast, useAuth } from "hooks";

const unauthorizedCode = [401];

const AxiosFetch = axios.create({
  timeout: 60000,
  baseURL: appConfig.apiPrefix,
});

AxiosFetch.interceptors.request.use(
  async (config) => {
    if (appConfig.runtime !== "production" && appConfig.enableMock) {
      return config;
    }

    const { userManager } = useAuth.getState();
    const user = await userManager.getUser();
    if (!user || user.expired) {
      toastAndRedirectToLogin();
      return Promise.reject();
    }
    config.headers["Authorization"] = `Bearer ${user.access_token}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

AxiosFetch.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    const { response } = error;
    if (response && unauthorizedCode.includes(response.status)) {
      toastAndRedirectToLogin();
    } else {
      toast({
        status: "error",
        title: error.message,
      });
    }
    return Promise.reject(error);
  },
);

const toastAndRedirectToLogin = () => {
  const { userManager } = useAuth.getState();
  toast({
    status: "error",
    title: "Please login to continue",
    onCloseComplete: () => void userManager.signinRedirect(),
  });
};

export default AxiosFetch;
