import { Injectable } from '@nestjs/common';
import { User } from '../models/user.model';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';

// This should be a real class/interface representing a user entity

@Injectable()
export class UsersService {
  constructor(
    @InjectModel(User.name)
    private userModel: Model<User>,
  ) {}

  async getUser(queryParams: Record<string, any>): Promise<User | null> {
    try {
      const user = await this.userModel.findOne(queryParams);

      return user;
    } catch (error) {
      throw error;
    }
  }

  async createUser(userDto: Partial<User>): Promise<User | null> {
    const newUser = await this.userModel.create(userDto);
    return newUser;
  }
}
