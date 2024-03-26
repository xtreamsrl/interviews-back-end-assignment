import {
  Body,
  Controller,
  Get,
  HttpCode,
  HttpStatus,
  Post,
  Request,
  UseGuards,
} from '@nestjs/common';
import { AuthGuard } from '../guard/auth.guard';
import { AuthService } from './auth.service';
import { AdminGuard } from '../guard/admin.guard';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) {}

  @HttpCode(HttpStatus.OK)
  @Post('login')
  signIn(@Body() signInDto: Record<string, any>) {
    return this.authService.signIn(signInDto.email, signInDto.password);
  }

  @HttpCode(HttpStatus.OK)
  @Post('register')
  register(@Body() registerDto: Record<string, any>) {
    return this.authService.register(registerDto);
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Get('profile')
  getProfile(@Request() req) {
    return req.user;
  }
}
