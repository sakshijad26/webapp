pipeline {
    agent any   // 👈 This means Jenkins can run on any available node, including the built-in master

    stages {

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Sonar-Report') {
            steps {
                bat '''
                    mvn clean install sonar:sonar ^
                    -Dsonar.host.url=http://localhost:9000 ^
                    -Dsonar.analysis.mode=publish
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution finished!'
        }
    }
}
