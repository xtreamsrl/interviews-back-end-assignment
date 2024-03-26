import { Injectable, UnauthorizedException } from '@nestjs/common';
import { UsersService } from '../users/users.service';
import { JwtService } from '@nestjs/jwt';
import { User } from '../models/user.model';

@Injectable()
export class AuthService {
  constructor(
    private usersService: UsersService,
    private jwtService: JwtService,
  ) {}

  async signIn(
    email: string,
    password: string,
  ): Promise<{ access_token: string }> {
    const user = await this.usersService.getUser({ email: email });

    if (!user) {
      throw new UnauthorizedException('Invalid user');
    }
    if (user?.password !== password) {
      throw new Error('Invalid password');
    }
    const payload = {
      ...user,
    };

    //after need to implement the refresh token
    return {
      access_token: await this.jwtService.signAsync(payload),
    };
  }

  async register(userDto: Partial<User>): Promise<User> {
    const user = this.usersService.createUser(userDto);
    return user;
  }
}
