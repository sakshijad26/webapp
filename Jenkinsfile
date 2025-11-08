pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonarqube-token')
        NEXUS_URL = 'http://localhost:8081/repository/upes/'
        MAVEN_SETTINGS = 'C:\\ProgramData\\Jenkins\\.jenkins\\.m2\\settings.xml'   // ✅ Correct file path
    }

    stages {

        stage('Checkout Source') {
            steps {
                echo '📥 Checking out source code...'
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Building the project...'
                bat "mvn -B -DskipTests clean package --settings \"${MAVEN_SETTINGS}\""
            }
        }

        stage('Run Tests') {
            steps {
                echo '🧪 Running tests...'
                bat "mvn test --settings \"${MAVEN_SETTINGS}\""
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Running SonarQube code analysis...'
                bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=webapp ^
                    -Dsonar.host.url=${SONARQUBE_URL} ^
                    -Dsonar.login=${SONAR_TOKEN} ^
                    --settings "${MAVEN_SETTINGS}"
                """
            }
        }

        stage('Check Maven Settings') {
            steps {
                echo '🧩 Verifying Maven is using the correct settings.xml...'
                bat "mvn help:effective-settings --settings \"${MAVEN_SETTINGS}\" > settings-output.txt"
                bat 'type settings-output.txt'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo '🚀 Deploying artifact to Nexus Repository...'
                
                // Use stored Jenkins credentials (nexus-cred)
                withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    bat """
                        mvn deploy -DskipTests ^
                        -DaltDeploymentRepository=upes::default::${NEXUS_URL} ^
                        --settings "${MAVEN_SETTINGS}"
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build, Test, SonarQube Analysis, and Nexus Deployment completed successfully!'
            echo "📦 Artifact deployed to: ${NEXUS_URL}"
        }
        failure {
            echo '❌ Build failed — please check the console logs for details.'
        }
    }
}
