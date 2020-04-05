import axios from 'axios'

let base = 'http://47.114.189.146:8081/api';
// let base = 'http://localhost:8081/api';
export const postRequest = (url, params) => {
  // let token = this.$global_msg.token;
  console.log(this.$global_msg)
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      // Do whatever you want to transform the data
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: getHttpHeader()
  });
}

export const uploadFileRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    headers: getHttpHeader()
  });
}

export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: getHttpHeader()
  });
}

export const deleteRequest = (url) => {
  return axios({
    method: 'delete',
    headers: getHttpHeader(),
    url: `${base}${url}`
  });
}

export const getRequest = (url, params) => {
  return axios({
    method: 'get',
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: getHttpHeader(),
    url: `${base}${url}`
  });
}

/***
 * token 身份校验
 * @returns {{Authorization: string, "Content-Type": string}|{"Content-Type": string}}
 */
export const getHttpHeader = () => {
  if (localStorage.getItem('token')) {
    return {
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': localStorage.getItem('token')
    }
  } else
    return {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
}
