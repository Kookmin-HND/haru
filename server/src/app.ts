import express, { Request, Response, NextFunction } from "express";
import myDataSource from "./app-data-source";
import router from "./routes";

// establish database connection
myDataSource
  .initialize()
  .then(() => {
    console.log("Data Source has been initialized!");
  })
  .catch((err) => {
    console.error("Error during Data Source initialization:", err);
  });

const app = express();
app.use(express.json());

app.get("/", (req: Request, res: Response, next: NextFunction) => {
  console.log("First page!");
  res.json({ "Frist page!": "hi" });
});

app.get("/welcome", (req: Request, res: Response, next: NextFunction) => {
  res.send("welcome!!");
});

//routes/index.ts 연결
//api 요청 경로 : localhost:8000/api/~~
app.use("/api", router);

app.listen("8000", () => {
  console.log(`
  ################################################
  🛡️  Server listening on port: 8000
  ################################################
`);
});
