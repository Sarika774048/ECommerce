import React from "react";
import {
  Avatar,
  Box,
  Button,
  IconButton,
  useMediaQuery,
  useTheme,
} from "@mui/material";

import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import FavoriteBorder from "@mui/icons-material/FavoriteBorder";
import AddShoppingCart from "@mui/icons-material/AddShoppingCart";
import Storefront from "@mui/icons-material/Storefront";

const Navbar = () => {
  const theme = useTheme();
  const isLarge = useMediaQuery(theme.breakpoints.up("lg"));

  return (
    <Box>
      <div className="flex items-center justify-between px-5 lg:px-20 h-[70px] border-b">
        {/* Left section */}
        <div className="flex items-center gap-2 lg:gap-6">
          <IconButton>
            <MenuIcon />
          </IconButton>

          <h1 className="logo cursor-pointer text-lg md:text-2xl text-[#00927c]">
            Infinity Cart
          </h1>
        </div>
        <ul className="flex items-center font-medium text-gray-800 *: gap-2 lg:gap-8">
        {
          ["Men", "Women", "Home & Furniture", "Electronics"].map( (item) => <li className = "mainCategory hover:text-[#00927c]">{item}</li>
          )
        }
        </ul>

        {/* Right section */}
        <div className="flex items-center gap-1 lg:gap-4">
          <IconButton>
            <SearchIcon />
          </IconButton>

          {true ? (
            <Button className="flex items-center gap-2">
              <Avatar
                sx={{ width: 29, height: 29 }}
                src="https://sarika-software-dev.vercel.app/sarika_raw.jpg"
              />
              <h1 className="font-semibold hidden lg:block">Sarika</h1>
            </Button>
          ) : (
            <Button variant="contained">Login</Button>
          )}

          <IconButton>
            <FavoriteBorder sx={{ fontSize: 29 }} />
          </IconButton>

          <IconButton>
            <AddShoppingCart
              className="text-gray-700"
              sx={{ fontSize: 29 }}
            />
          </IconButton>

          {isLarge && (
            <Button startIcon={<Storefront />} variant="outlined">
              Become Seller
            </Button>
          )}
        </div>
      </div>
    </Box>
  );
};

export default Navbar;