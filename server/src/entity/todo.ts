import { Entity, Column, PrimaryGeneratedColumn } from "typeorm";

//필요한 데이터베이스 스키마 entity에 생성

@Entity()
export class Todo {
  //id : auto Increment
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  writer: string;

  @Column()
  todoId: number;

  @Column()
  folder: string;

  @Column()
  content: string;

  @Column({ type: Date })
  createdAt: string;
}
