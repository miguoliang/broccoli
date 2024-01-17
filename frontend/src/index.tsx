import { appConfig } from "configs";
import { ToastContainer } from "hooks";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.css";

/**
 * Set enableMock(Default false) to true at configs/app.config.js
 * If you wish to enable mock api
 */
async function deferRender() {
  if (appConfig.runtime === "production" || !appConfig.enableMock) {
    return;
  }

  const { worker } = await import("./mock");

  // `worker.start()` returns a Promise that resolves
  // once the Service Worker is up and ready to intercept requests.
  console.log("Mocking enabled");
  return worker.start();
}

void deferRender().then(() => {
  const root = ReactDOM.createRoot(document.getElementById("root")!);
  root.render(
    <>
      <App />
      <ToastContainer />
    </>,
  );
});
