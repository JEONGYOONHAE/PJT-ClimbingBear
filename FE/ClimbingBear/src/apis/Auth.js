import EncryptedStorage from 'react-native-encrypted-storage';
import axios from 'axios';

const API_URL = 'http://k7d109.p.ssafy.io:8080/user';

// accessToken => encryptedStorage STORE
export const storeToken = async accessToken => {
  try {
    await EncryptedStorage.setItem('accessToken', accessToken);
  } catch (error) {
    console.log(error);
    console.log(error.message);
  }
};

// accessToken => encryptedStorage GET
export const getToken = async () => {
  try {
    const authHeader = await EncryptedStorage.getItem('accessToken');
    // console.log('authHeader', authHeader)
    return authHeader;
  } catch (error) {
    console.log(error);
    console.log(error.message);
  }
};

// acessToken => encryptedStorage REMOVE
export const removeToken = async () => {
  try {
    await EncryptedStorage.removeItem('accessToken');
  } catch (error) {
    console.log(error);
    console.log(error.message);
  }
};

// 로그인
export const postLogin = async (id, password) => {
  try {
    const response = await axios({
      method: 'post',
      url: API_URL + '/login',
      data: {
        id: id,
        pw: password,
      },
    });

    const accessToken = response.data.data.accessToken;
    storeToken(accessToken);

    if (response.data.status === 'success') {
      return response.data;
    }
  } catch (error) {
    console.log(error);
    console.log(error.response.data);
    return false;
  }
};

// nickname 중복검사
export const existNickname = async nickname => {
  try {
    const response = await axios({
      method: 'get',
      url: API_URL + `/nickname`,
      params: {
        nickName: nickname,
      },
    });

    if (response.data.data.isExist === true) {
      return true;
    } else if (response.data.data.isExist === false) {
      return false;
    }
  } catch (error) {
    console.log(error);
    console.log(error.response.data);
    console.log(error.response.headers);
  }
};

// id 중복검사
export const existId = async id => {
  try {
    const response = await axios({
      method: 'get',
      url: API_URL + `/email`,
      params: {
        id: id,
      },
    });

    if (response.data.data.isExist === true) {
      return true;
    } else if (response.data.data.isExist === false) {
      return false;
    }
  } catch (error) {
    console.log(error);
    console.log(error.response.data);
    console.log(error.response.headers);
  }
};

// 회원가입
export const postSignUp = async (id, nickname, password) => {
  try {
    const response = await axios({
      method: 'post',
      url: API_URL + `/signup`,
      data: {
        id: id,
        nickname: nickname,
        pw: password,
      },
    });

    if (response.data.status === 'success') {
      return true;
    }
  } catch (error) {
    console.log(error);
    console.log(error.response.data);
    return false;
  }
};
