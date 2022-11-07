import passport from "passport";
import bcrypt from "bcrypt";
import { User } from "../entity/user";
import myDataSource from "../app-data-source";
import { Strategy as LocalStrategy } from "passport-local";
import { ExtractJwt } from "passport-jwt";

const passportConfig = { usernameField: "email", passwordField: "password" };

const passportVerify = async (email: string, password: string, done: any) => {
  try {
    const user = await myDataSource
      .getRepository(User)
      .findOneBy({ email: email });

    if (!user) {
      return done(null, false, { reason: "존재하지 않는 사용자입니다." });
    }

    const result = await bcrypt.compare(password, user.password);

    if (!result) {
      return done(null, false, { reason: "올바르지 않은 비밀번호 입니다." });
    }
    return done(null, user);
  } catch (error) {
    console.error(error);
    done(error, false, null);
  }
};

/* 다른 해싱 방법
const salt = user.user_salt;
    const hashedPW = crypto
      .createHash("sha512")
      .update(password + salt)
      .digest("base64"); */

// const JWTConfig = {
//   jwtFromRequest : ExtractJwt.fromHeader('authorization'),
//   secretOrKey : ":YZiEm/viU5(2MD",
// };

const JWTconfig = {
  jwtFromRequest: ExtractJwt.fromHeader("authorization"),
  secretOrKey: process.env.JWT_KEY,
};

export default function passportOpt() {
  passport.use("local", new LocalStrategy(passportConfig, passportVerify));
}
