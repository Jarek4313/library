export interface IUser {
  login: string;
  email: string;
  role: string;
}

export class User implements IUser {
  constructor(
    public login: string,
    public email: string,
    public role: string
  ) {}
}

export interface LoginData {
  login: string;
  password: string;
}

export interface RegisterData {
  email: string;
  login: string;
  password: string;
}

export interface RegisterResponse {
  timestamp: string;
  message: string;
  code: string;
}

export interface LogoutResponse extends RegisterResponse {};

export interface LoggedInResponse extends Omit<RegisterResponse, 'message'> {
  message: boolean;
}