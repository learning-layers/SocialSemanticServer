# Daten einlesen
args <- commandArgs(TRUE)
filename <- args[1]
path <- args[2]
pid <- args[3]
samplefile <- paste(path, "csv/", filename, ".txt", sep = "")
trainfile <- paste(path, "csv/", filename, "_train.txt", sep = "")
testfile <- paste(path, "csv/", filename, "_test.txt", sep = "")
resultsfile <- paste(path, "results/", filename, "_3layers.txt", sep = "")

D_eighty <- read.table(trainfile,sep=";",fill=T) 

T_Res <- read.table(samplefile,sep=";",fill=T) 

T_Tags <- T_Res[,4]
TagList <- matrix(unlist(strsplit(as.matrix(T_Tags), ",")),ncol=1)
TagList2 <- c(unlist(TagList))
TagListFil <-matrix(TagList2[!duplicated(TagList2)],ncol=1)

T_Cats <- T_Res[,5]
CatList <- matrix(unlist(strsplit(as.matrix(T_Cats), ",")),nrow=1)
CatList2 <- c(unlist(CatList))
CatListFil <-matrix(CatList2[!duplicated(CatList2)],nrow=1)

T_Users <- D_eighty[,1]
UserList <- matrix(unlist(T_Users),ncol=1)
UserList2 <- c(unlist(UserList))
UserListFil <-matrix(UserList2[!duplicated(UserList2)],ncol=1)

D_twenty <- read.table(testfile,sep=";",fill=T)

T_U_tw <- D_twenty[,1]
L_U_tw <- matrix(unlist(T_U_tw),ncol=1)
L_U_tw2 <- c(unlist(L_U_tw))
L_U_tw_Fil <-matrix(L_U_tw2[!duplicated(L_U_tw2)],ncol=1)

T_U_ei <- D_eighty[,1]
L_U_ei <- matrix(unlist(T_U_ei),ncol=1)
L_U_ei2 <- c(unlist(L_U_ei))
L_U_ei_Fil <-matrix(L_U_ei2[!duplicated(L_U_ei2)],ncol=1)

############################################## Examples construction
CcInStrings <- strsplit(as.matrix(D_eighty[,5]),",")
# relative Häufigkeit der Kategorien
Cc<- function(j){  # Catcombs
  vec <- rep(0,times=length(CatListFil))
  cats <- match(unlist(strsplit(as.matrix(D_eighty[j,5]),",")),CatListFil)
  vec[as.numeric(names(table(cats)))] <- table(cats)/sum(table(cats))
  vec <- as.matrix(vec)
}
CatCombs <- sapply(c(1:nrow(D_eighty)),Cc) 
UniqueCc <- unique(CatCombs,MARGIN=2) # each col is a unique catcomb

CcsAsStrings <- function(i){toString(CatCombs[,i])}
CcsStrings <- sapply(c(1:ncol(CatCombs)),CcsAsStrings)
############################################### for smoothing
numbs <- table(match(TagList2,TagListFil))
prior <- rep(0,times=length(TagListFil))
prior[as.numeric(names(numbs))] <- numbs/sum(numbs)


PatternComp <- function(x) {
  u <- D_twenty[x,1]
  u <- which(D_eighty[,1]==u,arr.ind=T)
  res <- D_twenty[x,2]
  res <- which(D_eighty[,2]== toString(res),arr.ind=T)
  UtR <- D_eighty[res,1]    #Users who tagged same resource
  LUtR <- function(i){which(D_eighty[,1]==i)}
  LinesUtR <- unlist(sapply(UtR,LUtR))
  LinesRel <- c(u,res)
  
  NU <- length(unlist(strsplit(as.matrix(D_eighty[u,4]),",")))    # Number of tags available for user u
  NR <- length(unlist(strsplit(as.matrix(D_eighty[res,4]),",")))  # Number of tags available for resource res
  
  bookCats <- unlist(strsplit(as.matrix(D_twenty[x,5]),","))
  bookCatsNum <- match(bookCats,CatListFil)
  bookCatsTab <- table(bookCatsNum)
  Tags <- as.matrix(D_twenty[x,4])
  Tags <- unlist(strsplit(Tags[,1],","))
  TagsNum <- match(Tags,TagListFil)
  
  catVecLine <- rep(0, times=length(CatListFil))
  catVecLine[as.numeric(names(bookCatsTab))] <- bookCatsTab/sum(bookCatsTab)
  ##################################################### All examples
  AllExamples <- function(i){
    Cc <- unlist(strsplit(as.matrix(D_eighty[i,5]),","))
    CcNum <- match(Cc,CatListFil)
    CcNumSort <- sort(CcNum)
    CcTable <- table(CcNum)
    CcVec <- rep(0,times=length(CatListFil))
    CcVec[as.numeric(names(CcTable))] <- CcTable/sum(CcTable)
    CcAsString <- toString(CcVec)
    FoundCcs <- which(CcsStrings==CcAsString)
    Tags <- as.matrix(D_eighty[FoundCcs,4])
    Tags <- unlist(strsplit(Tags[,1],","))
    TagsNum <- match(Tags,TagListFil)
    List <- list(CcNumSort,TagsNum)
  }
  AllExPast <- sapply(LinesRel,AllExamples) # AllExPast[1,] = alle CatCombs, AllExPast[2,] = alle TagCombs
  ######################################## user's examples in past
  uCcsVecs <- function(i){
    Cc <- table(unlist(AllExPast[1,][i]))
    CcVec <- rep(0,times=length(CatListFil))  
    CcVec[as.numeric(names(Cc))] <- Cc/sum(Cc)
    CcVec <- as.matrix(CcVec)
  }
  UserCcsPast <- sapply(c(1:length(u)),uCcsVecs)
  
  uTaDisVecs <- function(i){
    TDis <- table(unlist(AllExPast[2,][i]))
    TDisVec <- rep(0,times=length(TagListFil))  
    TDisVec[as.numeric(names(TDis))] <- TDis/sum(TDis)
    TDisVec <- as.matrix(TDisVec)
  }
  UserTaDisPast <- sapply(c(1:length(u)),uTaDisVecs)
  
  sims <- function(i){cosine <- crossprod(catVecLine, UserCcsPast[,i])/sqrt(crossprod(catVecLine) * crossprod(UserCcsPast[,i]))}
  simis <- sapply(c(1:ncol(UserCcsPast)),sims)
  simis <- 1-simis # um Distanz zu erhalten
  
  ajhid <- exp(-simis)
  
  akout1 <- t(t(UserTaDisPast)*ajhid)
  nas <- which(is.na(akout1[1,]))
  if(length(nas)!=0){akout1[,nas]<-0}
  akout2 <- rowSums(akout1)           #Gleichung 2 zweiter Schritt
  denom <- sum(exp(akout2))
  PrK <- exp(akout2)/denom   
  
  ################################################# past CatCombs of resource
  
  ResCcsVecs <- function(i){
    Cc <- table(unlist(AllExPast[1,][i]))
    CcVec <- rep(0,times=length(CatListFil))  
    CcVec[as.numeric(names(Cc))] <- Cc/sum(Cc)
    CcVec <- as.matrix(CcVec)
  }
  ResCcsPast <- sapply(c((length(u)+1):length(LinesRel)),ResCcsVecs)
  
  ResTaDisVecs <- function(i){
    TDis <- table(unlist(AllExPast[2,][i]))
    TDisVec <- rep(0,times=length(TagListFil))  
    TDisVec[as.numeric(names(TDis))] <- TDis/sum(TDis)
    TDisVec <- as.matrix(TDisVec)
  }
  ResTaDisPast <- sapply(c((length(u)+1):length(LinesRel)),ResTaDisVecs)
  
  simsR <- function(i){cosine <- crossprod(catVecLine, ResCcsPast[,i])/sqrt(crossprod(catVecLine) * crossprod(ResCcsPast[,i]))}
  simisR <- sapply(c(1:ncol(ResCcsPast)),simsR)
  simisR <- 1-simisR # um Distanz zu erhalten
  
  ajhidR <- exp(-simisR)
  
  akout1R <- t(t(ResTaDisPast)*ajhidR)
  nasR <- which(is.na(akout1R[1,]))
  if(length(nasR)!=0){akout1R[,nasR]<-0}
  akout2R <- rowSums(akout1R)           #Gleichung 2 zweiter Schritt
  denomR <- sum(exp(akout2R))
  PrKR <- exp(akout2R)/denomR  
  
  PrKFinal <- PrK+PrKR
  
  ############################################### smoothing
  #pTR <- log2(NR+1)*PrKR + log2(NU+1)*prior
  #pTU <- log2(NU+1)*PrK + log2(NR+1)*prior
  #EFive <- (pTR*pTU)/prior
  ###############################################
  c <- order(PrKFinal,decreasing=T)
  recoC <- c[1:10]
  
  C <- length(recoC) # number of found items
  R <- length(TagsNum) # number of relevant items
  Rc <- length(intersect(recoC,TagsNum))# number found and relevant
  precC <- Rc/C
  recallC <- Rc/R
  FC <- 2*((precC*recallC)/(precC+recallC))
  namesRc <- toString(TagListFil[intersect(recoC,TagsNum)])
  #RC <- data.frame(x,prec=precC,recall=recallC,F=FC,length(recoC),length(TagsNum),namesRc)
  R2 <- data.frame(x,toString(TagsNum),toString(recoC))
  #write.table(RC,file="/Users/paulseitlinger/Desktop/3Layers2/results/3LayersII_measures.txt",append=TRUE,quote=FALSE,sep="|",row.names=FALSE,col.names=FALSE)
  write.table(R2,file=resultsfile,append=TRUE,quote=FALSE,sep="|",row.names=FALSE,col.names=FALSE)
  
}
sapply(c(1:nrow(D_twenty)),PatternComp)
