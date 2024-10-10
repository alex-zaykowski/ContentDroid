"use client";

import { ChangeEventHandler, useCallback, useState } from "react";
import { ToastContainer, ToastOptions, toast } from "react-toastify";

export default function Home() {
  const maxFileNameLength = 23;
  const fileNamePlaceholder = "Upload Background Video";
  const socket = new WebSocket("ws://localhost:8000");
  socket.addEventListener("open", function (event) {
    socket.send("Connection Established");
  });

  //change to be messages-${userid} instead of hard coded
  socket.addEventListener("messages-alex", function (event) {
    toast.info("Video Ready!", { ...toastProps });
  });

  const contactServer = () => {
    socket.send("Initialize");
  };
  const [fileDisplayName, setFileDisplayName] =
    useState<string>(fileNamePlaceholder);

  const toastProps: ToastOptions = {
    position: "bottom-right",
    autoClose: 5000,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light",
    onClick: () => window.location.assign("/account"),
  };

  const generateVideo = async (videoGenerationData: FormData) => {
    videoGenerationData.append("username", "alex");
    videoGenerationData.append(
      "date",
      new Date().toISOString().replace("T", " ").substring(0, 19),
    );

    fetch("http://127.0.0.1:8080/generate", {
      method: "POST",
      body: videoGenerationData,
    })
      .then((response) => {
        if (response.ok) {
          toast.info("Video Queued", { ...toastProps });
          return;
        }
      })
      .catch((err) => {
        toast.error(`Error: ${err}`, { ...toastProps });
      });
  };

  const formatFileName = (file: File | undefined) => {
    const name: string | undefined = file?.name;

    if (name === undefined) {
      return fileNamePlaceholder;
    }
    return name.length > maxFileNameLength
      ? `${name.substring(0, 20)}...`
      : name;
  };

  const updateFileDisplayName: ChangeEventHandler = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    if (event.target.files) {
      const file: File | undefined = event.target.files
        ? event.target.files[0]
        : undefined;

      const fileName: string | undefined = formatFileName(file);

      setFileDisplayName(fileName);
    }
  };

  const deleteDialogue = () => {
    const dialogue: HTMLInputElement | null = document.getElementById(
      "dialogue",
    ) as HTMLInputElement;

    if (dialogue) {
      dialogue.value = "";
    }
  };

  return (
    <div className="mt-52 flex flex-col">
      <form className="m-auto w-3/4 sm:w-3/5" action={generateVideo}>
        <label className="mb-4 block text-2xl font-bold sm:text-3xl">
          Video Generator
        </label>
        <div className="sm:flex sm:justify-between">
          <select id="underline_select" name="voice" className="voice-btn peer">
            <option value="en_us_001">Female US</option>
            <option value="en_us_006">Male US</option>
            <option value="en_au_001">Female AU</option>
            <option value="en_au_002">Male AU</option>
            <option value="en_uk_001">Male UK</option>
          </select>
          <input
            type="file"
            name="backgroundVideo"
            id="file"
            className="sr-only"
            onChange={updateFileDisplayName}
          />
          <label htmlFor="file">
            <span className="base-btn inline-flex bg-zinc-50">
              {fileDisplayName}
            </span>
          </label>
        </div>
        <textarea
          className="text-area"
          id="dialogue"
          name="dialogue"
          placeholder="Enter dialogue..."
        />
        <div className="mt-2 flex justify-between gap-3 sm:justify-end">
          <button
            type="button"
            onClick={deleteDialogue}
            className="base-btn bg-red-400 text-zinc-100 hover:bg-red-400 hover:text-zinc-50 sm:bg-red-300"
          >
            Delete
          </button>
          <input
            type="submit"
            className="base-btn bg-zinc-50 text-zinc-800 disabled:cursor-not-allowed disabled:hover:shadow-none"
            title="Generate"
            disabled={fileDisplayName === fileNamePlaceholder}
          />
        </div>
      </form>
    </div>
  );
}
