export function withConnection(type: string, name: string, paramType: string){
    return `public ${type} ${name}
    (${paramType} param)
    throws SQLException, ConnectionFailed
{
    return this.pool.withConnection((conn) -> {
        return _${name}(conn, param);
    });
}`;
}
