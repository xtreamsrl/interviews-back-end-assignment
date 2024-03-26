import {
  Body,
  Controller,
  Get,
  HttpCode,
  HttpStatus,
  Post,
  Request,
  Res,
  UseGuards,
} from '@nestjs/common';
import { AuthGuard } from '../guard/auth.guard';
import { AuthService } from './auth.service';
import { Response } from 'express';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) {}

  @HttpCode(HttpStatus.OK)
  @Post('login')
  signIn(@Body() signInDto: Record<string, any>, @Res() res: Response) {
    const login = this.authService.signIn(signInDto.email, signInDto.password);
    return res.status(HttpStatus.OK).json({
      message: 'login successfully',
      data: login,
    });
  }

  @HttpCode(HttpStatus.OK)
  @Post('register')
  register(@Body() registerDto: Record<string, any>, @Res() res: Response) {
    const user = this.authService.register(registerDto);
    return res.status(HttpStatus.OK).json({
      message: 'registration successfully registered',
      data: user,
    });
  }

  @UseGuards(AuthGuard)
  @Get('profile')
  getProfile(@Request() req, @Res() res: Response) {
    return res.status(HttpStatus.OK).json({
      message: 'profile found with success',
      data: req.user,
    });
  }
}
