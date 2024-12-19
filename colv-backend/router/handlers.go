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

	r.POST("/LogIn", dbh.HandleUserLogin)
	r.POST("/SignUp", dbh.HandleUserSignUp)

	r.GET("/UserPage/GetArticleAbstract", JWTAuthMiddleware(), dbh.HandleArticleAbstractRead)
	r.POST("/UserPage/PostArticle", JWTAuthMiddleware(), dbh.HandleArticlePost)
	r.GET("/UserPage/ReadArticle", JWTAuthMiddleware(), dbh.HandleArticleRead)
	r.PUT("/UserPage/ModifyArticle", JWTAuthMiddleware(), dbh.HandleAriticleModify)

	r.Run(":8088")
}

func (dbh *MysqlHandler) HandleUserLogin(c *gin.Context) {
	var usrLogin struct {
		Mail     string `json:"Mail" binding:"required"`
		PassWord string `json:"PassWord" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&usrLogin); err != nil {
		c.JSON(400, gin.H{"status": "error", "message": "invalid request"})
		return
	}

	// try to log in
	if user, err := dbh.DB.UserLogin(usrLogin.Mail, usrLogin.PassWord); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		token, err := GenerateToken(user.UID)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"status": "error", "message": "could not generate token"})
			return
		}
		// 返回 Token
		c.JSON(http.StatusOK, gin.H{"status": "success", "message": "log in succesfully", "uid": user.UID, "name": user.UserName, "token": token})
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
		c.JSON(400, gin.H{"status": "error", "message": "invalid request"})
		return
	}

	// try to sign up
	if err := dbh.DB.UserSignUp(usrSignup.Name, usrSignup.Mail, usrSignup.PassWord); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		c.JSON(200, gin.H{"status": "success", "message": "sign up successful"})
	}
}

func (dbh *MysqlHandler) HandleArticlePost(c *gin.Context) {
	var articlePost struct {
		Title   string `json:"Title" binding:"required"`
		Content string `json:"Content" binding:"required"`
	}

	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"status": "error", "message": "UID not found"})
		return
	}

	// bind json
	if err := c.ShouldBind(&articlePost); err != nil {
		c.JSON(400, gin.H{"status": "error", "message": "invalid request"})
		return
	}

	// try to sign up
	if err := dbh.DB.PostArticle(uint(userID.(float64)), articlePost.Title, articlePost.Content); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		c.JSON(200, gin.H{"status": "success", "message": "article post successful"})
	}
}

func (dbh *MysqlHandler) HandleArticleRead(c *gin.Context) {
	var articleRead struct {
		ArticleID uint `json:"ArticleID" binding:"required"`
	}

	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"status": "error", "message": "UID not found"})
		return
	}

	// bind json
	if err := c.ShouldBind(&articleRead); err != nil {
		c.JSON(400, gin.H{"status": "error", "message": "invalid request"})
		return
	}

	if article_data, err := dbh.DB.GetArticle(uint(userID.(float64)), articleRead.ArticleID); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		c.JSON(200, gin.H{"status": "success", "message": article_data})
	}
}

func (dbh *MysqlHandler) HandleArticleAbstractRead(c *gin.Context) {
	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"status": "error", "message": "UID not found"})
		return
	}

	if article_data, err := dbh.DB.FindAllArticlesAbstracts(uint(userID.(float64))); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		c.JSON(200, gin.H{"status": "success", "message": article_data})
	}
}

func (dbh *MysqlHandler) HandleAriticleModify(c *gin.Context) {
	userID, exists := c.Get("UID")
	if !exists {
		c.JSON(400, gin.H{"status": "error", "message": "UID not found"})
		return
	}

	var articleModify struct {
		AID     uint   `json:"ArticleID" binding:"required"`
		Title   string `json:"Title" binding:"required"`
		Content string `json:"Content" binding:"required"`
	}

	// bind json
	if err := c.ShouldBind(&articleModify); err != nil {
		c.JSON(400, gin.H{"status": "error", "message": "invalid request"})
		return
	}

	if err := dbh.DB.ModifyArticle(uint(userID.(float64)), articleModify.AID, articleModify.Title, articleModify.Content); err != nil {
		c.JSON(500, gin.H{"status": "error", "message": err.Error()})
	} else {
		c.JSON(200, gin.H{"status": "success", "message": "OK"})
	}
}
