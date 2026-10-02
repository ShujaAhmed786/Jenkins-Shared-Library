def call(Map config = [:]) {
    def dockerUser = config.dockerUser
    def appName    = config.appName
    def imageTag   = config.get('imageTag', 'latest')
    def credsId    = config.get('credentialsId', 'dockerhub-creds')
    def severity   = config.get('severity', 'HIGH,CRITICAL')

    def fullImage = "${dockerUser}/${appName}"

    stage('Docker Build') {
        sh "docker build -t ${fullImage}:${imageTag} -t ${fullImage}:latest ."
    }

    stage('Trivy Container Scan') {
        sh "trivy image --severity ${severity} --format table ${fullImage}:${imageTag}"
    }

    stage('Docker Push to Hub') {
        withCredentials([usernamePassword(credentialsId: credsId, usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
            sh '''
                echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
                docker push ''' + fullImage + ''':''' + imageTag + '''
                docker push ''' + fullImage + ''':latest
                docker logout
            '''
        }
    }
}
