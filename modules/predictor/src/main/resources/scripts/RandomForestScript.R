setwd("~/Documents/Machine Lerning/preprocessed data")

# Install and load required packages for decision trees and forests
library(rpart)
install.packages('randomForest')
library(randomForest)

Aggregate             <- read.csv("~/Documents/Machine Lerning/preprocessed data/Aggregate.csv")
AggregateCustomers    <- read.csv("~/Documents/Machine Lerning/preprocessed data/AggregateCustomers.csv")
AggregateNonCustomers <- read.csv("~/Documents/Machine Lerning/preprocessed data/AggregateNonCustomers.csv")

# splitdf function
splitdf <- function(dataframe, seed=NULL, splitratio) {
  if (!is.null(seed)) set.seed(seed)
  index <- 1:nrow(dataframe)
  trainindex <- sample(index, trunc(length(index)*splitratio))
  trainset <- dataframe[trainindex, ]
  testset <- dataframe[-trainindex, ]
  list(trainset=trainset,testset=testset)
}

splitCustomers    <- splitdf(AggregateCustomers, seed=808, splitratio = 0.7)

str(splitCustomers)
lapply(splitCustomers,nrow)
lapply(splitCustomers,head)

customerTrainSet  <- splitCustomers$trainset
customerTestSet   <- splitCustomers$testset

splitNonCustomers <- splitdf(AggregateNonCustomers, seed=808, splitratio = 0.7)

str(splitNonCustomers)
lapply(splitNonCustomers,nrow)
lapply(splitNonCustomers,head)

nonCustomerTrainSet <- splitNonCustomers$trainset
nonCustomerTestSet  <- splitNonCustomers$testset

#it returns a list - two data frames called trainset and testset
#str(splits)

#lapply(splits,nrow)

#view the first few columns in each data frame
#lapply(splits,head)

finalTrainSet <- rbind(customerTrainSet,nonCustomerTrainSet)
finalTestSet  <- rbind(customerTestSet,nonCustomerTestSet)

#fit the randomforest model
model <- randomForest(as.factor(Is.Customer) ~ seniorTitleCount + juniorTitleCount + totalActivities + Max.between.2.activities + Time.since.100th.activity + downloads , data=finalTrainSet, importance=TRUE, ntree=2000)

#print(mode)
# what are the important variables
varImpPlot(model)

#predict the outcome of the testing data
prediction <- predict(model, finalTestSet, type = "response")

#print(prediction1)

submit <- data.frame(companyName = finalTestSet$Company.Name, IsCustomer = prediction)
write.csv(submit, file = "prediction.csv", row.names = FALSE)

#fit <- randomForest(as.factor(Aggregate$Is.Customer) ~ Aggregate$seniorTitleCount + Aggregate$juniorTitleCount + Aggregate$totalActivities + Aggregate$Max.between.2.activities + Aggregate$Time.since.100th.activity , data=Aggregate, importance=TRUE, ntree=2000)