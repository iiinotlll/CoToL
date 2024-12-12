package router

import (
	"colv/sqldb"

	"github.com/gin-gonic/gin"
)

type MysqlHandler struct {
	DB *sqldb.MysqlDB
}

func GinStart() {
	r := gin.Default()
	dbh := &MysqlHandler{sqldb.MysqlDBInit()}

	r.GET("/Login", dbh.HandleUserLogin)
	r.POST("/SignUp", dbh.HandleUserSignUp)

	r.POST("/UserPage/PostArticle", dbh.HandleArticlePost)
	r.GET("/UserPage/ReadArticle", dbh.HandleArticleRead)

	r.Run(":8080")
}

func (dbh *MysqlHandler) HandleUserLogin(c *gin.Context) {
	var usrLogin struct {
		Mail     string `json:"Mail" binding:"required"`
		PassWord string `json:"PassWord" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&usrLogin); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to log in
	if _, err := dbh.DB.UserLogin(usrLogin.Mail, usrLogin.PassWord); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": "login successful"})
	}
}

func (dbh *MysqlHandler) HandleUserSignUp(c *gin.Context) {
	var usrSignup struct {
		Name     string `json:"Name" binding:"required"`
		Mail     string `json:"Mail" binding:"required"`
		PassWord string `json:"PassWord" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&usrSignup); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to sign up
	if err := dbh.DB.UserSignUp(usrSignup.Name, usrSignup.Mail, usrSignup.PassWord); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": "sign up successful"})
	}
}

func (dbh *MysqlHandler) HandleArticlePost(c *gin.Context) {
	var articlePost struct {
		UID     uint   `json:"UID" binding:"required"`
		Title   string `json:"Title" binding:"required"`
		Content string `json:"Content" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&articlePost); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to sign up
	if err := dbh.DB.PostArticle(articlePost.UID, articlePost.Title, articlePost.Content); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": "article post successful"})
	}
}

func (dbh *MysqlHandler) HandleArticleRead(c *gin.Context) {
	var articleRead struct {
		ArticleID uint `json:"ArticleID" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&articleRead); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to sign up
	if article_data, err := dbh.DB.GetArticle(articleRead.ArticleID); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": article_data})
	}
}
