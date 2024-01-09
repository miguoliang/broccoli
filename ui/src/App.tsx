import {DocumentEditor} from "@onlyoffice/document-editor-react";
import {useEffect} from "react";

const onDocumentReady = function () {
  console.log("Document is loaded");
};

const onLoadComponentError = function (errorCode: any, errorDescription: any) {
  switch (errorCode) {
    case -1: // Unknown error loading component
      console.log(errorDescription);
      break;

    case -2: // Error load DocsAPI from http://documentserver/
      console.log(errorDescription);
      break;

    case -3: // DocsAPI is not defined
      console.log(errorDescription);
      break;
  }
};

export default function App() {

  useEffect(() => {
    const formData = new FormData();
    formData.append("userName", "si_ls");
    formData.append("passWord", "6bc71055d9b6300403be6208eacf0c3b");
    fetch("https://tianjian.singhand.com/userLogin", {
      method: "POST",
      body: formData,
    }).then((res) => {
      console.log(res);
    });
  }, []);

  return (
    <pre>
        <DocumentEditor
          id="docxEditor"
          documentServerUrl="http://documentserver/"
          config={{
            "document": {
              "fileType": "docx",
              "key": "Khirz6zTPdfd7",
              "title": "Example Document Title.docx",
              "url": "https://example.com/url-to-example-document.docx"
            },
            "documentType": "word",
            "editorConfig": {
              "callbackUrl": "https://example.com/url-to-callback.ashx"
            }
          }}
          events_onDocumentReady={onDocumentReady}
          onLoadComponentError={onLoadComponentError}
        />
    </pre>
  );
}