import { Router } from "express";
import * as tests from "./tests.routes";
import * as posts from "./posts.routes";

const router = Router();
// router.use(example.path, example.router);
// 여기에 새로 만든 라우터를 import하고 추가하면 된다.
router.use(tests.path, tests.router);
router.use(posts.path, posts.router);

export default router;
