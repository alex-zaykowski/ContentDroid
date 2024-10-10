import type { Metadata } from "next";

import "./globals.css";
import { ToastContainer } from "react-toastify";
import NavBar from "@/components/Navbar";
import "react-toastify/dist/ReactToastify.css";

export const metadata: Metadata = {
  title: "Content Droid",
  description: "Automatically generate short-form videos.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="min-h-screen bg-gradient-to-b from-slate-50 to-slate-300">
        <ToastContainer
          position="bottom-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
        <NavBar />
        {children}
      </body>
    </html>
  );
}
