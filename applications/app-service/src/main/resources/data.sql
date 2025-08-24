INSERT INTO roles (name, description) 
VALUES ('admin', 'Administrador del sistema')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description) 
VALUES ('asesor', 'Asesor de clientes')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description) 
VALUES ('cliente', 'Cliente del sistema')
ON CONFLICT (name) DO NOTHING;

