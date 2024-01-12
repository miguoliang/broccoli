import {DocumentEditor} from "@onlyoffice/document-editor-react";

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

    return (
        <pre style={{height: '500px'}}>
        <DocumentEditor
            id="docxEditor"
            documentServerUrl="http://localhost:8000/"
            config={{
                "document": {
                    "fileType": "docx",
                    "key": "bbbb.docx",
                    "title": "Example Document Title.docx",
                    "url": "http://s3:9000/my-bucket/Aaaaaaaaaaa.docx",
                },
                "documentType": "word",
                "editorConfig": {
                    "callbackUrl": "http://host.docker.internal:8888/onlyoffice/callback"
                }
            }}
            events_onDocumentReady={onDocumentReady}
            onLoadComponentError={onLoadComponentError}
        />
    </pre>
    );
}