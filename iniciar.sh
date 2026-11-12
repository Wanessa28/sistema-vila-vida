
#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "================================="
echo "   INICIANDO SISTEMA VILA VIDA"
echo "================================="

# Verificar se o MySQL está funcionando
echo "Verificando MySQL..."

if ! sudo service mysql status >/dev/null 2>&1; then
    echo "Iniciando MySQL..."
    sudo service mysql start
fi

# Confirmar se o MySQL iniciou
if ! sudo service mysql status >/dev/null 2>&1; then
    echo "ERRO: MySQL não está disponível."
    exit 1
fi

echo "MySQL funcionando!"

# Verificar credenciais dos GitHub Codespaces Secrets
if [ -z "${VILAVIDA_DB_PASSWORD:-}" ] ||
   [ -z "${VILAVIDA_LOGIN_USUARIO:-}" ] ||
   [ -z "${VILAVIDA_LOGIN_SENHA:-}" ]; then
    echo "ERRO: Uma ou mais credenciais não estão configuradas."
    exit 1
fi

echo "Credenciais carregadas com sucesso!"
echo "Iniciando Spring Boot..."

exec ./mvnw spring-boot:run
