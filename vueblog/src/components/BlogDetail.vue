<template>
  <div class="home_container">
    <el-header>
      <div class="home_title">{{currentUserName}}的博客</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link home_userinfo">{{currentUserName}}
    <i class="el-icon-arrow-down el-icon--right home_userinfo "></i>
  </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="sysMsg" v-if="isLogin">系统消息</el-dropdown-item>
            <el-dropdown-item command="MyArticle" v-if="isLogin">我的文章</el-dropdown-item>
            <el-dropdown-item command="MyHome" v-if="isLogin">个人主页</el-dropdown-item>
            <el-dropdown-item command="logout" divided>{{isLogin?"退出登录":"登录"}}</el-dropdown-item>
            <el-dropdown-item command="Register" divided v-if="!isLogin">注册账号</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-row v-loading="loading" class="content-contanier">
      <el-col :span="24">
        <div style="text-align: left;">
          <!--<el-button type="text" icon="el-icon-back" @click="goBack" style="padding-bottom: 0px;">返回</el-button>-->
        </div>
      </el-col>
      <el-col :span="24">
        <div>
          <div><h3 style="margin-top: 0px;margin-bottom: 0px">{{article.title}}</h3></div>
          <div style="width: 100%;margin-top: 5px;display: flex;justify-content: flex-end;align-items: center">
            <div style="display: inline; color: #20a0ff;margin-left: 50px;margin-right:20px;font-size: 12px;">
              {{article.nickname}}
            </div>
            <span style="color: #20a0ff;margin-right:20px;font-size: 12px;">浏览 {{article.pageView==null?0:article.pageView}}</span>
            <span style="color: #20a0ff;margin-right:20px;font-size: 12px;"> {{article.editTime | formatDateTime}}</span>
            <el-tag type="success" v-for="(item,index) in article.tags" :key="index" size="small"
                    style="margin-left: 8px">{{item.tagName}}
            </el-tag>
            <span style="margin:0px 50px 0px 0px"></span>
          </div>
        </div>
      </el-col>
      <el-col>
        <div class="article-content" style="text-align: left" v-html="article.htmlContent">
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  import {getRequest} from '../utils/api'

  export default {
    methods: {
      goBack() {
        this.$router.go(-1);
      },
      handleCommand(command) {
        var _this = this;
        if (command == 'logout') {
          if (_this.isLogin) {
            this.$confirm('注销登录吗?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(function () {
              getRequest("/logout")
              _this.currentUserName = '游客';
              localStorage.setItem('token','');
              // _this.$router.replace({path: '/'});
            }, function () {
              //取消
            })
          } else {
            const {href} = this.$router.resolve({
              path: '/Login'
            })
            window.open(href, '_blank')
          }

        }else if(command == 'Register'){
          const {href} = this.$router.resolve({
            path: '/Register'
          })
          window.open(href, '_blank')
        }
      }
    },
    mounted: function () {
      var aid = this.$route.query.aid;
      this.activeName = this.$route.query.an
      var _this = this;
      this.loading = true;
      getRequest("/article/" + aid).then(resp => {
        if (resp.status == 200) {
          _this.article = resp.data;
        }
        _this.loading = false;
      }, resp => {
        _this.loading = false;
        _this.$message({type: 'error', message: '页面加载失败!'});
      });
      var _this = this;
      getRequest("/currentUserName").then(function (msg) {
        console.log(msg);
        if(msg.data.msg=="尚未登录，请登录!"){
          _this.currentUserName ='游客';
      }else{
          _this.currentUserName = msg.data;
          _this.isLogin = true
      }

      }, function (msg) {
        _this.currentUserName = '游客';
        _this.isLogin = false
      });
    },

    data() {
      return {
        article: {},
        loading: false,
        activeName: '',
        currentUserName: '',
        isLogin: false
      }
    }
  }
</script>
<style>
  .home_container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
  }

  .content-contanier {
    padding: 20px;
  }

  .el-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .el-main {
    background-color: #fff;
    color: #000;
    text-align: center;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    display: inline;
    margin-right: 20px;
  }
  .article-content p img{
    width: 100%;
  }
</style>
