<template>
  <div class=" recommend-header home_container">
    <el-header>
      <div class="home_title">文创海思</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link home_userinfo">更多
    <i class="el-icon-arrow-down el-icon--right home_userinfo "></i>
  </span>
          <el-dropdown-menu slot="dropdown">
            <!--<el-dropdown-item command="sysMsg" v-if="isLogin">系统消息</el-dropdown-item>-->
            <!--<el-dropdown-item command="MyArticle" v-if="isLogin">我的文章</el-dropdown-item>-->
            <el-dropdown-item command="MyHome" v-if="isLogin">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" divided>{{isLogin?"退出登录":"登录"}}</el-dropdown-item>
            <el-dropdown-item command="Register" divided v-if="!isLogin">注册账号</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <div class="recomment-part">
      <div class="recomment-side-tab"></div>
      <div class="recomment-item">
        <div class="header">
          <div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane label="站内推荐" name="first"></el-tab-pane>
              <el-tab-pane label="站外推荐" name="second"></el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <div style="display: flex; justify-content: flex-start ;margin-top: 6px;">
          <div class="el-input el-input--mini el-input--prefix" style="width: 400px; margin-top: 5px;"><!---->
            <input type="text" autocomplete="off" placeholder="通过标题搜索该分类下的博客..." class="el-input__inner">
            <span class="el-input__prefix">
            <i class="el-input__icon el-icon-search"></i>
          </span>
          </div>
          <button type="button" class="el-button el-button--primary el-button--mini" style="margin-left: 3px;"><!---->
            <i class="el-icon-search"></i>
            <span>搜索</span>
          </button>
        </div>

        <div class="recommend-content">
          <div class="recommend-list" v-for="articlesItem in articles" :key="articlesItem.id">
            <div class="list-content" @click="goRecommentDetails(articlesItem)">
              <img src="../../static/img/pic.jpg" alt="">
              <div>
                <h3>{{articlesItem.title}} {{articlesItem.id}}</h3>
                <p class="article">
                  {{articlesItem.summary}}</p>
              </div>
              <div class="recommend-data">
                <span>89.9</span>
                <span>杨幂</span>
                <span>1212</span>
                <span>789</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import {getRequest} from '../utils/api'

  export default {

    data() {
      return {
        article: {},
        loading: false,
        isLogin: false,
        activeName: 'first',
        input: '',
        currentUserName: '',
        articles: [],
        selItems: [],
        currentPage: 1,
        totalCount: -1,
        pageSize: 1,
        keywords: '',
        dustbinData: [],
        count: 100
      }
    },
    methods: {
      goLogin: function () {
        console.log(1);
        const {href} = this.$router.resolve({
          path: '/Login',
        })
        window.open(href, '_blank')
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
              localStorage.setItem('token', '');
            }, function () {
              //取消
            })
          } else {
            const {href} = this.$router.resolve({
              path: '/Login'
            })
            window.open(href, '_blank')
          }

        } else if (command == 'Register') {
          const {href} = this.$router.resolve({
            path: '/Register'
          })
          window.open(href, '_blank')
        } else if (command == 'MyHome') {
          const {href} = this.$router.resolve({
            path: '/Home'
          })
          window.open(href, '_blank')
        }
      },
      handleClick(tab, event) {
        if (tab.name == "first") {
            this.loadBlogs(1, this.count)
        } else if (tab.name == "second") {
          this.creeperData(1, 1)
        }
      },
      goRecommentDetails: function (item) {
        console.log(item);
        console.log(this.activeName);
        if (this.activeName == "second") {
          const {href} = this.$router.resolve({
            path: '/RecommendDetail',
            query: {id: item.id}
          })
          window.open(href, '_blank')
        } else {
          const {href} = this.$router.resolve({
            path: '/BlogDetail',
            query: {aid: item.id}
          })
          window.open(href, '_blank')
        }
      },
      creeperData(page, count) {
        var _this = this;
        this.state = 1;
        var url = '';
        url = "/article/reptiles?" + "page=" + 1 + "&count=" + 200 + "&keywords=" + this.keywords;
        getRequest(url).then(resp => {
          _this.articles = resp.data.articles;
        })
      },
      loadBlogs(page, count) {
        var _this = this;
        this.state = 1;
        var url = '';
        url = "/article/tuijian?" + "page=" + page + "&count=" + count + "&keywords=" + this.keywords;
        getRequest(url).then(resp => {
          _this.loading = false;
          if (resp.status == 200) {
            _this.articles = resp.data.articles;
            _this.totalCount = resp.data.totalCount;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp => {
          _this.loading = false;
          if (resp.response.status == 403) {
            _this.$message({type: 'error', message: resp.response.data});
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }).catch(resp => {
          //压根没见到服务器
          _this.loading = false;
          _this.$message({type: 'error', message: '数据加载失败!'});
        })
      },

    },
    mounted: function () {
      var _this = this;
      getRequest("/currentUserName").then(function (msg) {
        console.log(msg);
        if (msg.data.msg == "尚未登录，请登录!") {
          _this.currentUserName = '游客';
        } else {
          _this.currentUserName = msg.data;
          _this.isLogin = true
        }

      }, function (msg) {
        _this.currentUserName = '游客';
        _this.isLogin = false
      });
      this.loadBlogs(1, this.count);

      this.creeperData()
      var _this = this;
      window.bus.$on('blogTableReload', function () {
        _this.loading = true;
        _this.loadBlogs(_this.currentPage, _this.count);
      })
    },

  }
</script>
<style>
  /*a{*/
  /*color: #333;*/
  /*text-decoration: none;*/
  /*}*/

  .recommend-header {
    font-size: 14px;
    background-color: #fff;
    margin-top: 0;
  }

  .recommend-header .header {
    height: 40px;
    line-height: 40px;
    display: flex;
    padding-bottom: 10px;

  }

  .home_container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
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

  .recomment-part {
    width: 100%;
    height: 300px;
  }

  .recomment-side-tab {
    width: 200px;
    border: 2px solid #dddddd;
    height: 800px;
    float: left;
  }

  .recomment-item {
    margin-left: 210px;
    padding: 10px 20px;
  }

  .el-button {
    display: inline-block;
    /*line-height: 1;*/
    white-space: nowrap;
    cursor: pointer;
    background: #FFF;
    border: 1px solid #DCDFE6;
    color: #606266;
    text-align: center;
    box-sizing: border-box;
    outline: 0;
    margin: 0;
    transition: .1s;
    font-weight: 500;
    padding: 12px 20px;
    font-size: 14px;
    border-radius: 4px;
    margin-left: 3px;
    margin-top: 5px;
    height: 30px;
    line-height: 16px;
  }

  .el-button--mini, .el-button--mini.is-round {
    padding: 7px 15px;
  }

  .el-button--mini, .el-button--small {
    font-size: 12px;
    border-radius: 3px;
  }

  .el-button--primary {
    color: #FFF;
    background-color: #409EFF;
    border-color: #409EFF;
  }

  .el-input__inner {
    background-color: #f1f1f1;
  }

  .recommend-content {
    width: 100%;
    height: 300px;
    margin-top: 10px;
  }

  .list-content h3 {
    text-align: left;
  }

  .recommend-aside {
    width: 25rem;
    height: 300px;
    float: right;
  }

  .recommend-aside .creeper {
    height: 200px;
    border: 1px solid #000;
  }

  .recommend-aside .recommend-author {
    height: 200px;
    border: 1px solid #000;
    padding: 10px 25px 10px 10px;
    margin-top: 20px;
  }

  .author-list-header {
    font-size: 12px;
    color: #7b7b7b;
    height: 30px;
    line-height: 30px;
    display: flex;
    justify-content: space-between;
  }

  .recommend-list {
    margin-right: 26rem;
    overflow: hidden;
  }

  .recommend-list .list-content {
    border-bottom: 1px solid #ccc;
    float: left;
  }

  .recommend-list .article {
    text-align: left;
    height: 20px;
    width: 800px;
  }

  .recommend-list .recommend-data {
    height: 30px;
    line-height: 30px;
    vertical-align: middle;
  }

  .recommend-list .recommend-data span {
    margin-right: 30px;
    vertical-align: center;

  }

  .recommend-list img {
    height: 7rem;
    width: 7rem;
    float: right;
    vertical-align: text-bottom;

  }

  .recommend-data {
    display: flex;
    justify-content: flex-start;
  }

  .author-list {
    height: 30px;
    line-height: 30px;
  }

  .author-icon img {
    width: 24px;
    height: 24px;
    border-radius: 50px;
    border: 1px solid #000;
  }


</style>
