pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonarqube-token') 
        NEXUS_URL = 'http://localhost:8081/repository/upes/'
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Building the project...'
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Run Tests') {
            steps {
                echo '🧪 Running tests...'
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Running SonarQube code analysis...'
                bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=webapp ^
                    -Dsonar.host.url=${SONARQUBE_URL} ^
                    -Dsonar.login=${SONAR_TOKEN}
                """
            }
        }

        // ✅ NEW STAGE ADDED HERE
        stage('Check Maven Settings') {
            steps {
                echo '🧩 Checking which settings.xml Maven is using...'
                bat 'mvn help:effective-settings > settings-output.txt'
                bat 'type settings-output.txt'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo '🚀 Deploying artifact to Nexus Repository...'

                // Using stored Jenkins credentials (nexus-cred)
                withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    bat '''
                        mvn deploy -DskipTests ^
                        -DaltDeploymentRepository=upes::http://localhost:8081/repository/upes/ ^
                        -Dnexus.username=%NEXUS_USER% ^
                        -Dnexus.password=%NEXUS_PASS%
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build, Test, Sonar Analysis, and Nexus Deployment completed successfully!'
        }
        failure {
            echo '❌ Build failed — check console output for details.'
        }
    }
}
