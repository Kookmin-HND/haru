import passport from "passport";
import bcrypt from "bcrypt";
import { User } from "../entity/user";
import DB from "../app-data-source";
import { Strategy as LocalStrategy } from "passport-local";
import { Strategy } from "passport-jwt";

const passportConfig = {
  usernameField: "email",
  passwordField: "password",
  session: false,
};

const passportVerify = async (email: string, password: string, done: any) => {
  try {
    const user = await DB.getRepository(User).findOneBy({ email: email });

    if (!user) {
      console.log("존재하지 않는 사용자");
      done(null, false, { reason: "존재하지 않는 사용자입니다." });
    } else {
      if (!user.password) {
        return;
      }
      const result = await bcrypt.compare(password, user.password);

      if (!result) {
        console.log("올바르지 않은 비밀번호");
        done(null, false, { reason: "올바르지 않은 비밀번호 입니다." });
      } else done(null, user);
    }
  } catch (error) {
    console.error(error);
    done(error);
  }
};

/* 다른 해싱 방법
const salt = user.user_salt;
    const hashedPW = c
    rypto
      .createHash("sha512")
      .update(password + salt)
      .digest("base64"); */

const cookieExtractor = (req: any) => {
  const { token } = req.cookies;
  return token;
};

const JWTConfig = {
  jwtFromRequest: cookieExtractor,
  secretOrKey: process.env.JWT_KEY,
};

const JWTVerify = async (token: any, done: any) => {
  try {
    if (!token) {
      console.log("token이 없습니다.");
      return done(null, false, { reason: "token이 없습니다." });
    }

    const user = await DB.getRepository(User).findOneBy({ email: token.email });

    if (!user) {
      console.log("token과 맞는 user가 없습니다.");
      return done(null, false, { reason: "token과 맞는 user가 없습니다." });
    }

    return done(null, user);
  } catch (error) {
    console.error(error);
    done(error);
  }
};

export default function passportOpt() {
  passport.use("local", new LocalStrategy(passportConfig, passportVerify));
  passport.use("jwt", new Strategy(JWTConfig, JWTVerify));
}
