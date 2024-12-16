package router

import (
	"colv/sqldb"
	"net/http"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

type MysqlHandler struct {
	DB *sqldb.MysqlDB
}

func GinStart() {
	r := gin.Default()
	r.Use(cors.Default())
	dbh := &MysqlHandler{sqldb.MysqlDBInit()}

	r.POST("/Login", dbh.HandleUserLogin)
	r.POST("/SignUp", dbh.HandleUserSignUp)

	r.GET("/UserPage/GetArticleAbstract", JWTAuthMiddleware(), dbh.HandleArticleAbstractRead)
	r.POST("/UserPage/PostArticle", JWTAuthMiddleware(), dbh.HandleArticlePost)
	r.GET("/UserPage/ReadArticle", JWTAuthMiddleware(), dbh.HandleArticleRead)

	r.Run(":8088")
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
	if user, err := dbh.DB.UserLogin(usrLogin.Mail, usrLogin.PassWord); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		token, err := GenerateToken(user.UID)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "could not generate token"})
			return
		}
		// 返回 Token
		c.JSON(http.StatusOK, gin.H{
			"UID":   user.UID,
			"token": token,
		})
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
		Title   string `json:"Title" binding:"required"`
		Content string `json:"Content" binding:"required"`
	}

	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"error": "UID not found"})
		return
	}

	// bind json
	if err := c.ShouldBind(&articlePost); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to sign up
	if err := dbh.DB.PostArticle(uint(userID.(float64)), articlePost.Title, articlePost.Content); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": "article post successful"})
	}
}

func (dbh *MysqlHandler) HandleArticleRead(c *gin.Context) {
	var articleRead struct {
		ArticleID uint `json:"ArticleID" binding:"required"`
	}

	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"error": "UID not found"})
		return
	}

	// bind json
	if err := c.ShouldBind(&articleRead); err != nil {
		c.JSON(400, gin.H{"error": "invalid request"})
		return
	}

	// try to sign up
	if article_data, err := dbh.DB.GetArticle(uint(userID.(float64)), articleRead.ArticleID); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": article_data})
	}
}

func (dbh *MysqlHandler) HandleArticleAbstractRead(c *gin.Context) {
	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"error": "UID not found"})
		return
	}

	// try to sign up
	if article_data, err := dbh.DB.FindAllArticlesAbstracts(uint(userID.(float64))); err != nil {
		c.JSON(500, gin.H{"error": err.Error()})
	} else {
		c.JSON(200, gin.H{"message": article_data})
	}
}
