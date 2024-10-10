"use client";
import { useState, useEffect } from "react";
import { saveAs } from "file-saver";

const Account: React.FC = () => {
  const [videos, setVideos] = useState<any[]>([]);

  const saveVideo = (url: string) => {
    saveAs(url, "video.mp4");
  };

  useEffect(() => {
    console.log("fetching videos...");
    const getVideos = async () => {
      const req = await fetch("http://127.0.0.1:8080/alex/videos", {
        method: "GET",
      });

      const data = (await req.json()) as any[];
      console.log(data);
      setVideos(data);
    };
    getVideos();
  }, []);

  return (
    <div>
      <h1>Account</h1>
      <ul>
        {videos.map((e, index) => {
          return (
            <li key={index}>
              <a
                key={index}
                href={"http://localhost:3000/videos/" + e.videoName}
                download="video.mp4"
              >
                {e.name ?? "video.mp4"}
              </a>
            </li>
          );
        })}
      </ul>
    </div>
  );
};

export default Account;
