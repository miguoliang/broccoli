# Knowledge you should have before using this project

---

## How to use variables in dynamic import with Vite

First of all, you should update build.dynamicImportOptions in vite.config.ts as below:

```ts
build: {
  dynamicImportOptions: {
    include: ['**']
  }
}
```

Then, you can use variable in dynamic import as below:

```ts
const { default: Component } = await import(`./${name}.tsx`)
```

Additionally, if you want to import raw content, you should add `?raw` to the end of the path:

```ts
const content = await import(`./${name}.md?raw`)
```

## Don't use default value and listen for its change in useEffect when you place the component in a Formik Field as a component

If you use default value and listen for its change in useEffect, the component will be re-rendered when the form is
submitted. This is because the component is re-rendered when the default value is changed. It sounds well, but you will
get Maximum update depth exceeded error when you place this component in a Formik Field.

So, you should listen and use the value in your custom component like below:

```tsx
const CustomField = ({ fileList, field, form, ...props }: FieldProps & { fileList: string[] }) => {
  const [files, setFiles] = useState<string[]>(fileList)
  useEffect(() => {
    setFiles(fileList);
  }, [fileList]);
  return <>
    <input {...field} {...props} />
    <ul>{
      files.map((file, index) => {
        return <li key={index}>{file}</li>
      })
    }</ul>
  </>
}
```

In the above example, you should not use `fileList` as a default value of `files` state. You should use `fileList` only
when the `fileList` is changed. The most important thing is that you should not set a default value for `fileList` in
declaration. Otherwise, a maximum update depth error will occur.

## What's the difference between `access_token` and `id_token` in Amazon Cognito?

Important: If there are no additional scopes configured on the API Gateway method, make sure that you're using a valid
ID token. If additional scopes are configured on the API Gateway method, confirm that you're using a valid access token.
For more information, see Integrate a REST API with an Amazon Cognito user pool and using Amazon Cognito custom scopes
in API Gateway.

## `React.StrictMode` causes `useEffect` to be called twice, thus causing the oidc-client to remove state twice.
In order to fix this, you should wrap the `React.StrictMode` with `AuthProvider` as below:

```tsx
<AuthProvider>
  <React.StrictMode>
    <App />
  </React.StrictMode>
</AuthProvider>
```