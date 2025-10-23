//1. 필요한 훅(hook)들을 import 합니다.
import React,{useState} from 'react'

import { useNavigate , Link as RouterLink} from 'react-router-dom';

import axios from 'axios'; //api 통신


import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';
import FormControl from '@mui/material/FormControl';
import Link from '@mui/material/Link';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import MuiCard from '@mui/material/Card';
import { styled } from '@mui/material/styles';
import AppTheme from '../shared-theme/AppTheme';
import ColorModeSelect from '../shared-theme/ColorModeSelect';
import { GoogleIcon, FacebookIcon, SitemarkIcon } from './components/CustomIcons';

const Card = styled(MuiCard)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignSelf: 'center',
  width: '100%',
  padding: theme.spacing(4),
  gap: theme.spacing(2),
  margin: 'auto',
  boxShadow:
    'hsla(220, 30%, 5%, 0.05) 0px 5px 15px 0px, hsla(220, 25%, 10%, 0.05) 0px 15px 35px -5px',
  [theme.breakpoints.up('sm')]: {
    width: '450px',
  },
  ...theme.applyStyles('dark', {
    boxShadow:
      'hsla(220, 30%, 5%, 0.5) 0px 5px 15px 0px, hsla(220, 25%, 10%, 0.08) 0px 15px 35px -5px',
  }),
}));

const SignUpContainer = styled(Stack)(({ theme }) => ({
  height: 'calc((1 - var(--template-frame-height, 0)) * 100dvh)',
  minHeight: '100%',
  padding: theme.spacing(2),
  [theme.breakpoints.up('sm')]: {
    padding: theme.spacing(4),
  },
  '&::before': {
    content: '""',
    display: 'block',
    position: 'absolute',
    zIndex: -1,
    inset: 0,
    backgroundImage:
      'radial-gradient(ellipse at 50% 50%, hsl(210, 100%, 97%), hsl(0, 0%, 100%))',
    backgroundRepeat: 'no-repeat',
    ...theme.applyStyles('dark', {
      backgroundImage:
        'radial-gradient(at 50% 50%, hsla(210, 100%, 16%, 0.5), hsl(220, 30%, 5%))',
    }),
  },
}));

export default function SignUp(props) {

  //1. 폼 입력값을 저장 할 상태
  const [name , setName] = useState('');
  const [email , setEmail] = useState('');
  const [password , setPassword] = useState('');

  //2. 유효성 검사 에러를 저장할 상태
  const [nameError, setNameError] = useState(false);  
  const [nameErrorMessage , setNameErrorMessage] = useState('');

  const [emailError, setEmailError] = useState(false);
  const [emailErrorMessage , setEmailErrorMessage] = useState('');

  const [passwordError , setPasswordError] = useState(false);
  const [passwordErrorMessage , setPasswordErrorMessage] = useState('');
  
  //3. API 서버 통신 실패 시 에러를 저장할 상태
  const [apiError , setApiError] = useState('');
  
  //4. 페이지 이동을 위한 훅
  const navigate = useNavigate();

  //1. axios.post를 기다리기 위해 async로 변경
  const handleSubmit = async (event) =>{
    //2. 페이지 새로고침 방지
    event.preventDefault();

    //3. 유효성 검사 (validateInputs 로직)---
    let isValid = true;
    if(!name){
      setNameError(true);
      setNameErrorMessage('아이디를 입력해주세요.');
      isValid = false;
    }else{
      setNameError(false);
      setNameErrorMessage('');
    }

    if(!email || !/\S+@\S+\.\S+/.test(email)){
      setEmailError(true);
      setEmailErrorMessage('이메일 형식으로 입력해주세요.');
      isValid = false;
    }else{
      setEmailError(false);
      setEmailErrorMessage('');
    }

    if(!password || password.length < 6){
      setPasswordError(true);
      setPasswordErrorMessage('비밀번호를 7자 이상으로 입력시켜주세요.');
      isValid(false);
    }else{
      setPasswordError(false);
      setPasswordErrorMessage('');
    }

    if(!isValid){
      return; //4. 유효성 검사 실패 시 여기서 중단
    }

    //5. API 호출 (axios)

    setApiError(''); //이전 API 에러 초기화
    try{
      const response = await axios.post('/api/auth/signup' , {
        name : name ,
        email : email ,
        password : password,
      });

      console.log('회원가입 성공 : ' , response.data);

      //6. 성공 시 로그인 페이지로 이동
      navigate('/signin');
      
    }catch(err){
      //7. 실패 시 (예 : 이메일 중복 ) 에러 메시지 설정
      console.error('회원가입 실패' , err);
      setApiError(err.response?.data?.message || '회원 가입에 실패하셨습니다.');
    }
  }

  return (
    <AppTheme {...props}>
      <CssBaseline enableColorScheme />
      <ColorModeSelect sx={{ position: 'fixed', top: '1rem', right: '1rem' }} />
      <SignUpContainer direction="column" justifyContent="space-between">
        <Card variant="outlined">
          <SitemarkIcon />
          <Typography
            component="h1"
            variant="h4"
            sx={{ width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)' }}
          >
            회원가입
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}
          >
            <FormControl>
              <FormLabel htmlFor="name">Full name</FormLabel>

              <TextField 
                autoComplete="name"
                name="name"
                required
                fullWidth
                id="name"
                placeholder="Jon Snow"
                error={nameError}
                helperText={nameErrorMessage}
                color={nameError ? 'error' : 'primary'}
                value = {name}
                onChange ={(e) => setName(e.target.value)}
              />

            </FormControl>
            <FormControl>
              <FormLabel htmlFor="email">Email</FormLabel>
              <TextField
                required
                fullWidth
                id="email"
                placeholder="your@email.com"
                name="email"
                autoComplete="email"
                variant="outlined"
                error={emailError}
                helperText={emailErrorMessage}
                color={passwordError ? 'error' : 'primary'}
                value = {email}
                onChange ={(e) => setEmail(e.target.value)}
              />
            </FormControl>
            <FormControl>
              <FormLabel htmlFor="password">Password</FormLabel>
              <TextField
                required
                fullWidth
                name="password"
                placeholder="••••••"
                type="password"
                id="password"
                autoComplete="new-password"
                variant="outlined"
                error={passwordError}
                helperText={passwordErrorMessage}
                color={passwordError ? 'error' : 'primary'}
                value = {password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </FormControl>
            <FormControlLabel
              control={<Checkbox value="allowExtraEmails" color="primary" />}
              label="I want to receive updates via email."
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
            >
              회원가입
            </Button>
          </Box>
          <Divider>
            <Typography sx={{ color: 'text.secondary' }}>or</Typography>
          </Divider>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Button
              fullWidth
              variant="outlined"
              onClick={() => alert('Sign up with Google')}
              startIcon={<GoogleIcon />}
            >
              Sign up with Google
            </Button>
            <Button
              fullWidth
              variant="outlined"
              onClick={() => alert('Sign up with Facebook')}
              startIcon={<FacebookIcon />}
            >
              Sign up with Facebook
            </Button>
            <Typography sx={{ textAlign: 'center' }}>
              이미 계정이 있으십니까?{' '}
              <Link
                href="/SignInPage"
                variant="body2"
                sx={{ alignSelf: 'center' }}
              >
                로그인
              </Link>
            </Typography>
          </Box>
        </Card>
      </SignUpContainer>
    </AppTheme>
  );
}