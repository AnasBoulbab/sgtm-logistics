# GitLab CI/CD variables

The pipeline relies on a few project-level CI/CD variables. Add them in GitLab under **Settings ➜ CI/CD ➜ Variables**.

## Registry access
- `DOCKER_USERNAME` (protected, masked): Docker Hub username used by the `login` template to push images.
- `DOCKER_PASSWORD` (protected, masked): Access token or password for the Docker Hub account.

## Cloud deploy
- `DEPLOY_HOST` (protected, masked): SSH host (e.g., `vps.example.com`) that receives the cloud deployment.
- `DEPLOY_USER` (protected, masked): SSH user on the VPS (e.g., `ubuntu`); the job connects as `${DEPLOY_USER}@${DEPLOY_HOST}`.
- `SSH_PRIVATE_KEY` (protected, masked): Private key that allows the runner to SSH/rsync to the VPS; should correspond to a public key in `~/.ssh/authorized_keys` for `DEPLOY_USER`.
- `DEPLOY_TARGET_DIR` (optional): Override for the remote project directory name (defaults to `$CI_PROJECT_NAME`).

The deploy job installs `openssh-client` and `rsync`, authenticates with `SSH_PRIVATE_KEY`, creates `~/DEPLOY_TARGET_DIR` on the VPS if missing, copies `infrastructure/cloud` assets (`docker-compose.yml`, `.env`, `mosquitto/`, `postgres/`), and runs `docker compose up -d` from that directory.