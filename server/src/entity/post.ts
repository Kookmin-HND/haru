import { Entity, Column, PrimaryGeneratedColumn, CreateDateColumn, Generated, UpdateDateColumn, OneToMany, JoinTable, OneToOne, JoinColumn, ManyToOne } from "typeorm";
import { Comment } from "./comment";
import { ImageFile } from "./imageFile";
import { PostLike } from "./postLike";
import { User } from "./user";

//필요한 데이터베이스 스키마 entity에 생성

@Entity()
export class Post {
  //id : auto Increment
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(()=> User, (user)=>user.posts)
  @JoinColumn()
  user: User;

  @Column({ nullable: false })
  category: string;

  @Column({ nullable: false })
  content: string;

  //댓글과 일대다 연결, 게시물이 삭제되면 댓글도 삭제되도록 cascade
  @OneToMany(() => Comment, (comment) => comment.post, {
    cascade: true,
  })
  @JoinTable()
  comments: Comment[];

  //이미지와 일대다 연결, 게시물이 삭제되면 이미지도 삭제되도록 cascade
  @OneToMany(() => ImageFile, (imagefile) => imagefile.post, {
    cascade: true,
  })
  @JoinTable()
  imageFiles: ImageFile[];


  //좋아요와 일대다 연결, 게시물이 삭제되면 좋아요도 삭제되도록 cascade
  @OneToMany(() => PostLike, (like) => like.post, {
    cascade: true,
  })
  @JoinTable()
  likes: PostLike[];


  @CreateDateColumn({
    type: "timestamp",
    default: () => "CURRENT_TIMESTAMP(6)",
  })
  createdAt: Date;

  @UpdateDateColumn({
    type: "timestamp",
    default: () => "CURRENT_TIMESTAMP(6)",
    onUpdate: "CURRENT_TIMESTAMP(6)",
  })
  updatedAt: Date;
}
