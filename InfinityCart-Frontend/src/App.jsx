import React from "react";
import Navbar from "./components/Navbar";
import { ThemeProvider } from "@mui/material/styles";
import { customTheme } from "./Theme/customTheme";

const App = () => {
  return (
    <ThemeProvider theme={customTheme}>
      <div>
      <Navbar />
      </div>
    </ThemeProvider>
  );
};

export default App;