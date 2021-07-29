const env = process.env.NODE_ENV || "development";
const path = require("path");
const CopyWebpackPlugin = require("copy-webpack-plugin");

module.exports = {
  /* to configure ui/public as a static assets folder */
  configureWebpack: {
    optimization: {
      // !IMPORTANT to prevent name mangling!
      minimize: false
    },
    plugins: [
      new CopyWebpackPlugin([
        {
          from: path.join(__dirname, "./src/main/public/public"),
          to: path.join(__dirname, "target/ROOT/static"),
          toType: "dir",
          ignore: ["index.html", ".DS_Store"],
        },
      ]),
    ],
  },
  publicPath: "./",
  pwa: {
    iconPaths: {
      favicon32: "favicon/favicon-32x32.png",
    },

    manifestOptions: {
      name: "Topicus Track & Trace",
      short_name: "Track & Trace",
      theme_color: "#2563EB",
      background_color: "#2563EB",
      start_url: "/",
      display: "standalone",
      orientation: "portrait",
      icons: [
        {
          src: "./favicon/favicon-32x32.png",
          sizes: "32x32",
          type: "image/png",
        },
      ],
    },
  },
  outputDir: path.resolve(__dirname, "./target/ROOT"),
  assetsDir: "static",
  pages: {
    index: {
      entry: "src/main/public/main.ts",
      template: "src/main/public/public/index.html",
      // output as dist/index.html
      filename: "index.html",
      // when using title option,
      // template title tag needs to be <title><%= htmlWebpackPlugin.options.title %></title>
      title: "Topicus Track & Trace",
      // chunks to include on this pages, by default includes
      // extracted common chunks and vendor chunks.
      chunks: ["chunk-vendors", "chunk-common", "index"],
    },
  },
};
if (env == "development") {
  module.exports.configureWebpack.devtool = "source-map";
}
